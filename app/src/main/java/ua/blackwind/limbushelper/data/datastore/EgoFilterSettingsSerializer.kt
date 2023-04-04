package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.blackwind.limbus_helper.EgoFilterSettings
import java.io.InputStream
import java.io.OutputStream

class EgoFilterSettingsSerializer: Serializer<EgoFilterSettings.EgoSettings> {
    override val defaultValue: EgoFilterSettings.EgoSettings
        get() = EgoFilterSettings.EgoSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): EgoFilterSettings.EgoSettings {
        return try {
            EgoFilterSettings.EgoSettings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("EgoFilter settings data has been corrupted.")
        }
    }

    override suspend fun writeTo(t: EgoFilterSettings.EgoSettings, output: OutputStream) {
        t.writeTo(output)
    }
}