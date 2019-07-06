Spring boot soap webservices
======================================

WSDL URL 
-------------------------------------------------------------------------
http://localhost:8093/ws/countries.wsdl

SOAP Request 
-------------------------------------------------------------------------
<x:Envelope xmlns:x="http://schemas.xmlsoap.org/soap/envelope/" xmlns:gsp="http://localhost:8093/guides/gs-producing-web-service">
    <x:Header/>
    <x:Body>
        <gsp:getCountryRequest>
            <gsp:name>Spain</gsp:name>
        </gsp:getCountryRequest>
    </x:Body>
</x:Envelope>



End point URL 
-----------------------------------------------------------------
http://localhost:8990/ws


Success Response 
-----------------------------------------------------------------

<x:Envelope xmlns:x="http://schemas.xmlsoap.org/soap/envelope/" xmlns:gsp="http://localhost:8093/guides/gs-producing-web-service">
    <x:Header/>
    <x:Body>
        <gsp:getCountryRequest>
            <gsp:name>Spain</gsp:name>
        </gsp:getCountryRequest>
    </x:Body>
</x:Envelope>


Reference
-----------------------------------------------------------------
https://github.com/spring-guides/gs-producing-web-service
 
