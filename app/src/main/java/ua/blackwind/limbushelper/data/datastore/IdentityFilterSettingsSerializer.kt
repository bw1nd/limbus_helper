package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.blackwind.limbus_helper.IdentityFilterSettings
import java.io.InputStream
import java.io.OutputStream

class IdentityFilterSettingsSerializer: Serializer<IdentityFilterSettings.IdentitySettings> {
    override val defaultValue: IdentityFilterSettings.IdentitySettings
        get() = IdentityFilterSettings.IdentitySettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): IdentityFilterSettings.IdentitySettings {
        return try {
            IdentityFilterSettings.IdentitySettings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Corrupted data")
        }
    }

    override suspend fun writeTo(
        t: IdentityFilterSettings.IdentitySettings,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}