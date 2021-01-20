import org.buldakov.huawei.modem.model.Message
import org.buldakov.huawei.modem.model.SmsListResponse
import org.buldakov.huawei.modem.xml.getXmlMapper
import org.joda.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DeserializationTest {

    @Test
    fun deserializeTest() {

        val payloads =
            """
            <response>
                <Count>1</Count>
                    <Messages>
                        <Message>
                            <Smstat>1</Smstat>
                            <Index>40006</Index>
                            <Phone>+447922584844</Phone>
                            <Content>Тест </Content>
                            <Date>2021-01-19 21:32:38</Date>
                            <Sca></Sca>
                            <SaveType>0</SaveType>
                            <Priority>0</Priority>
                            <SmsType>1</SmsType>
                        </Message>
	                </Messages>
            </response>
        """

        val message =
            """
                        <Message>
                            <Smstat>1</Smstat>
                            <Index>40006</Index>
                            <Phone>+447711554422</Phone>
                            <Content>Test</Content>
                            <Date>2021-01-19 21:32:38</Date>
                            <Sca></Sca>
                            <SaveType>0</SaveType>
                            <Priority>0</Priority>
                            <SmsType>1</SmsType>
                        </Message>
                        """

        val xmlMapper = getXmlMapper()

        xmlMapper.readValue(payloads, SmsListResponse::class.java)

        val result = xmlMapper.readValue(message, Message::class.java)
        assertEquals(
            Message(
                40006,
                "+447711554422",
                "Test",
                LocalDateTime("2021-01-19T21:32:38")
            ),
            result
        )
    }
}
