package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.blackwind.limbus_helper.FilterModeSettings
import java.io.InputStream
import java.io.OutputStream

class FilterModeSettingsSerializer: Serializer<FilterModeSettings.FilterMode> {
    override val defaultValue: FilterModeSettings.FilterMode
        get() = FilterModeSettings.FilterMode.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FilterModeSettings.FilterMode {
        return try {
            FilterModeSettings.FilterMode.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("FilterModeSettings data is corrupted")
        }
    }

    override suspend fun writeTo(t: FilterModeSettings.FilterMode, output: OutputStream) {
        t.writeTo(output)
    }
}