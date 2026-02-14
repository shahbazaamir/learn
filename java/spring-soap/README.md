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



Excellent question — testing a **SOAP endpoint** is a bit different from REST because SOAP uses **XML envelopes** and a **WSDL (Web Services Description Language)** file to describe its operations.

Here’s a complete guide 👇

---

## 🧩 1️⃣ Understanding SOAP basics

* SOAP = **Simple Object Access Protocol**.
* Communication is **XML over HTTP** (sometimes HTTPS).
* Each request/response is wrapped in an **envelope** with:

    * `<soap:Envelope>` root
    * `<soap:Header>` optional
    * `<soap:Body>` required

Example request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.example.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getEmployeeDetails>
         <employeeId>123</employeeId>
      </ser:getEmployeeDetails>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## 🧪 2️⃣ Tools to test SOAP endpoints

### ✅ **Option 1 — SoapUI (most common)**

1. Download **[SoapUI](https://www.soapui.org/downloads/)** (free or Pro version).
2. Open SoapUI → *File → New SOAP Project*.
3. Enter the **WSDL URL** (e.g. `http://localhost:8080/employeeService?wsdl`).
4. SoapUI will automatically create:

    * Requests for all operations.
    * Sample XML body.
5. Modify the request data and click **Run ▶️**.

🟢 Output: full SOAP response in XML.

---

### ✅ **Option 2 — Postman**

Postman can also test SOAP:

1. Create a **new POST request**.
2. Set **URL** to your SOAP endpoint (e.g. `http://localhost:8080/employeeService`).
3. Go to **Headers**:

   ```
   Content-Type: text/xml
   SOAPAction: "getEmployeeDetails"
   ```
4. In **Body**, select **raw → XML** and paste the SOAP envelope.
5. Click **Send**.

You’ll see an XML response (the SOAP reply).

---

### ✅ **Option 3 — curl (command line)**

You can test SOAP directly with curl:

```bash
curl -X POST http://localhost:8080/employeeService \
     -H "Content-Type: text/xml;charset=UTF-8" \
     -H "SOAPAction: getEmployeeDetails" \
     -d @request.xml
```

Where `request.xml` contains your SOAP envelope (as shown earlier).

---

### ✅ **Option 4 — JUnit test in Java**

If you want to automate SOAP tests:

```java
import org.junit.jupiter.api.Test;
import org.springframework.ws.client.core.WebServiceTemplate;
import jakarta.xml.transform.stream.StreamSource;
import java.io.StringReader;

public class SoapClientTest {

    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Test
    void testGetEmployeeDetails() {
        String xml = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:ser="http://service.example.com/">
               <soapenv:Body>
                  <ser:getEmployeeDetails>
                     <employeeId>123</employeeId>
                  </ser:getEmployeeDetails>
               </soapenv:Body>
            </soapenv:Envelope>
            """;

        StreamSource source = new StreamSource(new StringReader(xml));
        StreamSource response = (StreamSource) webServiceTemplate
                .sendSourceAndReceive("http://localhost:8080/employeeService", source, null);

        // Print or assert the XML response
        System.out.println("Response: " + response);
    }
}
```

---

## 📦 3️⃣ Key things to check

| Check                         | Why it matters                                   |
| ----------------------------- | ------------------------------------------------ |
| **Content-Type** = `text/xml` | SOAP requires XML                                |
| **SOAPAction** header         | Identifies which operation to invoke             |
| **WSDL**                      | Defines available operations, input/output types |
| **Envelope namespaces**       | Must match what WSDL defines                     |
| **Response codes**            | 200 = OK, 500 = SOAP Fault                       |

---

## ⚠️ 4️⃣ Troubleshooting

| Error                        | Cause                           | Fix                                   |
| ---------------------------- | ------------------------------- | ------------------------------------- |
| `415 Unsupported Media Type` | Missing or wrong `Content-Type` | Use `text/xml`                        |
| `500 Internal Server Error`  | Invalid SOAP envelope           | Validate against WSDL                 |
| `SOAP Fault`                 | Business error from service     | Check `<faultstring>` inside response |

---

✅ **In summary:**

| Method             | Best For              | Tool       |
| ------------------ | --------------------- | ---------- |
| SoapUI             | Full-featured testing | GUI        |
| Postman            | Quick checks          | GUI        |
| curl               | CLI testing           | Terminal   |
| WebServiceTemplate | Automated tests       | Java JUnit |

---

If you want, I can give you an **end-to-end example project** — Spring Boot SOAP web service + SOAP client + test (SoapUI / curl).

Would you like me to generate that setup?
