package curtin.edu.assignment2.data

import android.content.Context
import android.preference.PreferenceManager
//key for the query preference, used any time the query value is read or written
private const val PREF_SEARCH_QUERY = "searchQuery"

/**
 * Singleton class used to store the last search result even when the
 * application is closed and re-opened
 */
object QueryPreferences {

    fun getStoredQuery(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}