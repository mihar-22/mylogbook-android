package com.mylb.mylogbook.presentation.device.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.sync.CarSync
import com.mylb.mylogbook.data.sync.SupervisorSync
import com.mylb.mylogbook.data.sync.TripSync
import com.mylb.mylogbook.domain.cache.UserCache
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber

class SyncAdapter (
        context: Context,
        private val userCache: UserCache,
        private val carSync: CarSync,
        private val supervisorSync: SupervisorSync,
        private val tripSync: TripSync,
        autoInitialize: Boolean = true,
        allowParallelSyncs: Boolean = false
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    override fun onPerformSync(
            account: Account,
            extras: Bundle,
            authority: String,
            provider: ContentProviderClient,
            syncResult: SyncResult
    ) {
        carSync.build()
                .concatWith(supervisorSync.build())
                .concatWith(tripSync.build())
                .subscribeWith(SyncObserver())
    }

    private inner class SyncObserver : CompletableObserver {

        override fun onComplete() {
            Timber.d("Syncing complete")

            userCache.lastSyncedAt = Network.now
        }

        override fun onSubscribe(d: Disposable) { Timber.d("Syncing in progress") }

        override fun onError(e: Throwable) { Timber.d("Syncing failed with %s:", e.message) }

    }

    companion object {
        const val AUTHORITY = "com.mylb.mylogbook.provider"
        const val SYNC_INTERVAL = 60L * 1440L
    }

}
