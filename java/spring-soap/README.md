Spring boot soap webservices
======================================


<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:gs="http://www.baeldung.com/springsoap/gen">
    <soapenv:Header/>
    <soapenv:Body>
        <gs:getCountryRequest>
            <gs:name>Spain</gs:name>
        </gs:getCountryRequest>
    </soapenv:Body>
</soapenv:Envelope>

http://localhost:8990/ws

https://www.baeldung.com/spring-boot-soap-web-service

https://github.com/eugenp/tutorials/blob/master/pom.xml