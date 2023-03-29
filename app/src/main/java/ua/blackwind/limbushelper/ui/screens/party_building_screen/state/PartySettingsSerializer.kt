package ua.blackwind.limbushelper.ui.screens.party_building_screen.state

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.blackwind.limbus_helper.PartySettingsOuterClass.PartySettings
import java.io.InputStream
import java.io.OutputStream

class PartySettingsSerializer: Serializer<PartySettings> {
    override val defaultValue: PartySettings
        get() = PartySettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PartySettings {
        return try {
            PartySettings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Corrupted data")
        }
    }

    override suspend fun writeTo(t: PartySettings, output: OutputStream) {
        t.writeTo(output)
    }
}