import org.buldakov.huawei.modem.model.DeleteSmsRequest
import org.buldakov.huawei.modem.model.SendSmsRequest
import org.buldakov.huawei.modem.xml.getXmlMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SerializationTest {

    @Test
    fun serializationDeleteSmsRequestTest() {

        val request = DeleteSmsRequest(listOf(1, 2, 3))

        val result = getXmlMapper().writeValueAsString(request)

        assertEquals("<request><Index>1</Index><Index>2</Index><Index>3</Index></request>", result)
    }

    @Test
    fun serializationSendSmsRequestTest() {

        val message = "Test message"
        val request = SendSmsRequest(
            phones = listOf("+1234567890"),
            content = message,
            length = message.length,
            date = "2021-01-19T21:32:38"
        )

        val result = getXmlMapper().writeValueAsString(request)

        assertEquals(
            "<request><Index>-1</Index><Phones><Phone>+1234567890</Phone></Phones><Sca></Sca><Content>Test message</Content><Length>12</Length><Reserved>1</Reserved><Date>2021-01-19T21:32:38</Date></request>",
            result
        )
    }
}
