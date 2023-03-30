package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.blackwind.limbus_helper.FilterSettings.FilterDrawerSheetSettings

import java.io.InputStream
import java.io.OutputStream

class FilterSettingsSerializer: Serializer<FilterDrawerSheetSettings> {
    override val defaultValue: FilterDrawerSheetSettings
        get() = FilterDrawerSheetSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FilterDrawerSheetSettings {
        return try {
            FilterDrawerSheetSettings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Corrupted data")
        }
    }

    override suspend fun writeTo(
        t: FilterDrawerSheetSettings,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}