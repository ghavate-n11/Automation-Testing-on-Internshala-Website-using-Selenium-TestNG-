## Automation Testing on Internshala Website using Selenium & TestNG

This project demonstrates automated testing of the **Internshala** website using **Selenium WebDriver** with **TestNG** as the test framework. It covers end-to-end testing scenarios like login, registration, internship search, and form validation.

---

## 📌 Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack Used](#tech-stack-used)
- [Setup Instructions](#setup-instructions)
- [Test Cases Covered](#test-cases-covered)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [Author](#author)

---

## 📖 About the Project

This project automates the user flow and functionality checks on the Internshala platform. The aim is to:
- Validate UI elements
- Automate repetitive manual testing tasks
- Provide faster feedback with TestNG reports
- Ensure reliability and robustness of the Internshala web interface

---

## 💻 Tech Stack Used

- **Language**: Java  
- **Automation Tool**: Selenium WebDriver  
- **Testing Framework**: TestNG  
- **Build Tool**: Maven  
- **IDE**: IntelliJ IDEA / Eclipse  
- **Browser**: Chrome (via ChromeDriver)  

---

## ⚙️ Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/ghavate-n11/Automation-Testing-on-Internshala.git
   cd Automation-Testing-on-Internshala
Import the project into your IDE (Eclipse/IntelliJ)

Install dependencies using Maven:

mvn clean install
Run the test suite:


mvn test
View the TestNG reports:

Navigate to the /test-output folder in your project directory.

✅ Test Cases Covered
✅ Login functionality with valid/invalid credentials

✅ Internship search by location/category

✅ Form validation (e.g., empty fields, invalid email format)

✅ Navigation testing (Home → Login → Dashboard)

✅ UI element verification (buttons, links, dropdowns)

🗂 Project Structure

Automation-Testing-on-Internshala/
│
├── src/test/java
│   ├── testcases/
│   └── utilities/
│
├── drivers/
│   └── chromedriver.exe
│
├── testng.xml
├── pom.xml
└── README.md

🙋‍♂️ Contributing
Contributions are welcome! Feel free to open issues or pull requests.

👨‍💻 Author
Nilesh Bhausaheb Ghavate

📧 Email: nileshghavate11@gmail.com

🔗 LinkedIn: linkedin.com/in/nileshghavate-203b27251
