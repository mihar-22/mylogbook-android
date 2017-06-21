package com.mylb.mylogbook.data.sync

import com.mylb.mylogbook.domain.delivery.local.repository.LocalRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteRepository
import com.mylb.mylogbook.domain.resource.Resource
import io.reactivex.Completable
import io.reactivex.rxkotlin.combineLatest
import org.joda.time.DateTime

abstract class BaseSync<T>(
        private val lastSyncedAt: DateTime?,
        private val localRepository: LocalRepository<T>,
        private val remoteRepository: RemoteRepository<T>
) where T : Resource {

    private val localResources = localRepository.all().firstElement().toObservable().replay()

    private val remoteResources = {
        val all = if (lastSyncedAt != null) remoteRepository.all(lastSyncedAt) else remoteRepository.all()

        all.map { it.data!! }.replay()
    }()

    private val allResources
        get() = localResources.combineLatest(remoteResources)

    fun build(): Completable = pull().concatWith(push()).doOnSubscribe { execute() }

    private fun execute() {
        localResources.connect()
        remoteResources.connect()
    }

    private fun pull() = pullInsertions().concatWith(pullUpdates()).concatWith(pullDeletions())

    private fun pullInsertions() = allResources
            .map { (locals, remotes) ->
                remotes.filter { remote -> !(locals.any { it.remoteId == remote.remoteId }) }
            }
            .map { localRepository.insert(it) }
            .ignoreElements()

    private fun pullUpdates() = allResources
            .map { (locals, remotes) ->
                remotes.filter { remote ->
                    val local = locals.firstOrNull { it.remoteId == remote.remoteId }

                    if (local != null) remote.id = local.id

                    (local != null) && (local.updatedAt < remote.updatedAt)
                }
            }
            .map { localRepository.update(it) }
            .ignoreElements()

    private fun pullDeletions() = allResources
            .map { (locals, remotes) ->
                remotes.filter { remote ->
                    val local = locals.firstOrNull { it.remoteId == remote.remoteId }

                    (local != null) && (local.deletedAt == null && remote.deletedAt != null)
                }
            }
            .map { localRepository.delete(it) }
            .ignoreElements()

    private fun push() = pushInsertions().concatWith(pushUpdates()).concatWith(pushDeletions())

    private fun pushInsertions() = localResources
            .flatMapIterable { it }
            .filter { it.remoteId == 0 }
            .flatMap { remoteRepository.create(it) }
            .ignoreElements()

    private fun pushUpdates() = localResources
            .flatMapIterable { it }
            .filter { (it.remoteId != 0) && (it.updatedAt > lastSyncedAt) }
            .flatMap { remoteRepository.update(it) }
            .ignoreElements()

    private fun pushDeletions() = localResources
            .flatMapIterable { it }
            .filter { (it.remoteId != 0) && (it.deletedAt != null && it.deletedAt!! > lastSyncedAt) }
            .flatMap { remoteRepository.delete(it) }
            .ignoreElements()

}
