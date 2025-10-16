# 🧾 Receipt Text Extraction Service

The **Receipt Text Extraction Service** is an intelligent system designed to automatically extract and normalize receipt data from images using **Amazon Textract**. It simplifies data entry and record-keeping by transforming printed receipts into structured digital data that can be stored, analyzed, or integrated into other business systems.

---

## 🌟 Purpose

This project aims to:
- **Digitize paper receipts** through OCR (Optical Character Recognition).
- **Automatically detect and extract key fields** such as store name, branch, cashier, manager, items, subtotal, and total amounts.
- **Normalize raw text** into a structured JSON format for easy integration with databases and APIs.
- **Support data persistence** using a MySQL database for long-term storage and retrieval.

---

## 🧠 How It Works

1. **Upload a Receipt Image**  
   Users or applications upload a scanned or photographed image of a receipt.

2. **Amazon Textract Processing**  
   The image is processed using AWS Textract to extract text lines.

3. **Text Normalization**  
   Extracted text is parsed and converted into a structured format:
   ```json
   {
     "companyName": "SM HYPERMARKET",
     "branch": "Quezon City",
     "managerName": "Eric Steer",
     "cashierNumber": "#3",
     "items": [
       { "productName": "Apple", "quantity": 1, "price": 9.20 },
       { "productName": "Gardenia", "quantity": 1, "price": 19.20 }
     ],
     "subTotal": 107.60,
     "cash": 200.00,
     "change": 92.40
   }
4. **Database Storage**
    The normalized data is saved into a MySQL database for future use, analytics, or auditing.

---

## 🏗️ Core Feature
- 🧠 AI-powered OCR with Amazon Textract
- 📄 Text normalization and receipt data extraction
- 💾 Data persistence with Spring Boot + MySQL
- 🧰 Environment-based configuration via .env
- 🧩 REST API-ready for integration with other services
- 📊 Structured output suitable for reports and analytics

---

## 🔒 Privacy & Security
- No image or text data is shared outside your AWS environment.
- Credentials and sensitive configurations are stored securely in a .env file.
- The system is compliant with data protection best practices.

---

## 🚀 Potential Use Cases
- Expense tracking and reimbursement systems
- Retail analytics and inventory management
- Accounting and auditing automation
- Digital archiving of paper receipts

## 🧩 Technology Stack
- Java 21 / Spring Boot
- Amazon Textract (AWS SDK)
- MySQL for data storage
- Lombok for cleaner entity models
- spring-dotenv for environment configuration
- Springdoc OpenAPI (Swagger UI) for API documentation