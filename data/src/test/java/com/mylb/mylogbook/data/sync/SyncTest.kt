package com.mylb.mylogbook.data.sync

import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.delivery.local.repository.LocalRepository
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteRepository
import com.mylb.mylogbook.domain.resource.Resource
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import io.reactivex.Observable
import org.amshove.kluent.shouldEqual
import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SyncTest {

    private val now = Network.now
    private val emptyList = listOf<TestEntity>()

    @Mock lateinit private var mockLocalRepository: LocalRepository<TestEntity>
    @Mock lateinit private var mockRemoteRepository: RemoteRepository<TestEntity>

    @Test
    fun PullInsertions_NewEntities_InsertedLocally() {
        val newEntities = listOf<TestEntity>(
                TestEntity(name = "Entity1", remoteId = 1),
                TestEntity(name = "Entity2", remoteId = 2),
                TestEntity(name = "Entity3", remoteId = 3)
        )

        executeSync(emptyList, newEntities)

        verify(mockLocalRepository).insert(newEntities)
        verify(mockLocalRepository).update(emptyList)
        verify(mockLocalRepository).delete(emptyList)
        verify(mockRemoteRepository, never()).create(any())
        verify(mockRemoteRepository, never()).update(any())
        verify(mockRemoteRepository, never()).delete(any())
    }

    @Test
    fun PullUpdates_NewUpdates_UpdatedLocally() {
        val localCopies = listOf<TestEntity>(
                TestEntity(name = "Entity1", remoteId = 1, updatedAt = now, id = 1),
                TestEntity(name = "Entity2", remoteId = 2, updatedAt = now, id = 2),
                TestEntity(name = "Entity3", remoteId = 3, updatedAt = now, id = 3)
        )

        val newUpdates = listOf<TestEntity>(
                TestEntity(name = "UpdatedEntity1", remoteId = 1 , updatedAt = now.plusHours(1)),
                TestEntity(name = "UpdatedEntity2", remoteId = 2, updatedAt = now.plusHours(1)),
                TestEntity(name = "UpdatedEntity3", remoteId = 3, updatedAt = now.plusHours(1))
        )

        executeSync(localCopies, newUpdates)

        verify(mockLocalRepository).insert(emptyList)
        verify(mockLocalRepository).update(newUpdates)
        verify(mockLocalRepository).delete(emptyList)
        verify(mockRemoteRepository, never()).create(any())
        verify(mockRemoteRepository, never()).update(any())
        verify(mockRemoteRepository, never()).delete(any())

        newUpdates.forEachIndexed { index, testEntity -> testEntity.id.shouldEqual(localCopies[index].id) }
    }

    @Test
    fun PullDeletions_NewDeletions_DeletedLocally() {
        val localCopies = listOf<TestEntity>(
                TestEntity(name = "Entity1", remoteId = 1),
                TestEntity(name = "Entity2", remoteId = 2),
                TestEntity(name = "Entity3", remoteId = 3)
        )

        val newDeletions = listOf<TestEntity>(
                TestEntity(name = "DeletedEntity1", remoteId = 1, deletedAt = now),
                TestEntity(name = "DeletedEntity2", remoteId = 2, deletedAt = now),
                TestEntity(name = "DeletedEntity3", remoteId = 3, deletedAt = now)
        )

        executeSync(localCopies, newDeletions)

        verify(mockLocalRepository).insert(emptyList)
        verify(mockLocalRepository).update(emptyList)
        verify(mockLocalRepository).delete(newDeletions)
        verify(mockRemoteRepository, never()).create(any())
        verify(mockRemoteRepository, never()).update(any())
        verify(mockRemoteRepository, never()).delete(any())
    }

    @Test
    fun PushInsertions_NewInsertions_InsertedRemotely() {
        val localCopies = listOf<TestEntity>(
                TestEntity(name = "Entity1"),
                TestEntity(name = "Entity2"),
                TestEntity(name = "Entity3")
        )

        given(mockRemoteRepository.create(localCopies[0])).willReturn(buildResponse(TestEntity(remoteId = 1)))
        given(mockRemoteRepository.create(localCopies[1])).willReturn(buildResponse(TestEntity(remoteId = 2)))
        given(mockRemoteRepository.create(localCopies[2])).willReturn(buildResponse(TestEntity(remoteId = 3)))

        executeSync(localCopies, emptyList)

        verify(mockLocalRepository).insert(emptyList)
        verify(mockLocalRepository).update(emptyList)
        verify(mockLocalRepository).delete(emptyList)
        verify(mockRemoteRepository, times(localCopies.size)).create(isA<TestEntity>())
        verify(mockRemoteRepository, never()).update(any())
        verify(mockRemoteRepository, never()).delete(any())
    }

    @Test
    fun PushUpdates_NewUpdates_UpdatedRemotely() {
        val localCopies = listOf<TestEntity>(
                TestEntity(name = "Entity1", remoteId = 1, updatedAt = now.plusHours(1)),
                TestEntity(name = "Entity2", remoteId = 2, updatedAt = now.plusHours(1)),
                TestEntity(name = "Entity3", remoteId = 3, updatedAt = now.plusHours(1))
        )

        given(mockRemoteRepository.update(any())).willReturn(buildResponse(Unit))

        executeSync(localCopies, emptyList)

        verify(mockLocalRepository).insert(emptyList)
        verify(mockLocalRepository).update(emptyList)
        verify(mockLocalRepository).delete(emptyList)
        verify(mockRemoteRepository, never()).create(any())
        verify(mockRemoteRepository, times(localCopies.size)).update(isA<TestEntity>())
        verify(mockRemoteRepository, never()).delete(any())
    }

    @Test
    fun PushDeletions_NewDeletions_DeletedRemotely() {
        val localCopies = listOf<TestEntity>(
                TestEntity(name = "Entity1", remoteId = 1, deletedAt = now.plusHours(1)),
                TestEntity(name = "Entity2", remoteId = 2, deletedAt = now.plusHours(1)),
                TestEntity(name = "Entity3", remoteId = 3, deletedAt = now.plusHours(1))
        )

        given(mockRemoteRepository.delete(any())).willReturn(buildResponse(Unit))

        executeSync(localCopies, emptyList)

        verify(mockLocalRepository).insert(emptyList)
        verify(mockLocalRepository).update(emptyList)
        verify(mockLocalRepository).delete(emptyList)
        verify(mockRemoteRepository, never()).create(any())
        verify(mockRemoteRepository, never()).update(any())
        verify(mockRemoteRepository, times(localCopies.size)).delete(isA<TestEntity>())
    }

    private fun executeSync(localCopies: List<TestEntity>, remoteCopies: List<TestEntity>) {
        val lastSyncedAt = Network.now

        given(mockLocalRepository.all()).willReturn(Flowable.just(localCopies))
        given(mockRemoteRepository.all(lastSyncedAt)).willReturn(buildResponse(remoteCopies))

        TestSync<TestEntity>(lastSyncedAt, mockLocalRepository, mockRemoteRepository)
                .build()
                .blockingGet()
    }

    private fun <T> buildResponse(data : T) = Observable.just(Response<T>("", data, null))

    private class TestSync<T : Resource>(
            lastSyncedAt: DateTime,
            localRepository: LocalRepository<T>,
            remoteRepository: RemoteRepository<T>
    ) : BaseSync<T>(lastSyncedAt, localRepository, remoteRepository)

    private class TestEntity(
            var name: String = "",
            override var id: Int = 0,
            override var remoteId: Int = 0,
            override var updatedAt: DateTime = Network.now,
            override var deletedAt: DateTime? = null
    ) : Resource

}
