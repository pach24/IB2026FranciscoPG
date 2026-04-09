package com.iberdrola.practicas2026.FranciscoPG.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.feedbackDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "feedback_prefs"
)

@Singleton
class FeedbackRepositoryImpl @Inject constructor(
    private val context: Context
) : FeedbackRepository {

    private val exitCountKey = intPreferencesKey("exit_count")
    private val lastInteractionKey = stringPreferencesKey("last_interaction")

    override suspend fun getExitCount(): Int {
        val count = context.feedbackDataStore.data
            .map { prefs -> prefs[exitCountKey] ?: 0 }
            .first()
        Log.d(TAG, "getExitCount() = $count")
        return count
    }

    override suspend fun setExitCount(count: Int) {
        Log.d(TAG, "setExitCount($count)")
        context.feedbackDataStore.edit { prefs ->
            prefs[exitCountKey] = count
        }
    }

    override suspend fun getLastInteraction(): FeedbackInteraction {
        val value = context.feedbackDataStore.data
            .map { prefs -> prefs[lastInteractionKey] ?: FeedbackInteraction.NONE.name }
            .first()
        val interaction = try {
            FeedbackInteraction.valueOf(value)
        } catch (_: IllegalArgumentException) {
            FeedbackInteraction.NONE
        }
        Log.d(TAG, "getLastInteraction() = $interaction (raw=$value)")
        return interaction
    }

    override suspend fun setLastInteraction(interaction: FeedbackInteraction) {
        Log.d(TAG, "setLastInteraction($interaction)")
        context.feedbackDataStore.edit { prefs ->
            prefs[lastInteractionKey] = interaction.name
        }
    }

    companion object {
        private const val TAG = "FeedbackRepo"
    }
}