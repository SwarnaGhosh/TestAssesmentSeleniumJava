# TestAssesmentSeleniumJava

Purpose
- End-to-end UI test automation using Selenium WebDriver, TestNG, and Page Object Model (POM).
- Includes functional flows and security edge-case checks (XSS, injection, session/cookie checks).

Quick Start
- Clone:
- git clone <your-repo-url>
  cd TestAssesmentSeleniumJava
- Run all tests:
  git clone <your-repo-url>
  cd TestAssesmentSeleniumJava
- Run all tests:
   mvn clean test
- Run a single test:
  mvn -Dtest=tests.LoginTests#errorMessageSanitizationTest test
- Serve Allure report (if Allure CLI installed):
  allure serve allure-results


Prerequisites
- JDK 11+ with `JAVA_HOME` set.
- Maven installed and on `PATH`.
- Browser driver (ChromeDriver/GeckoDriver) matching your browser, or use WebDriverManager.
- (Optional) Allure CLI for visual reports.

Important Files
- **pom**: [pom.xml](pom.xml) — dependencies and plugins.
- **TestNG suite**: [testng.xml](testng.xml).
- **Tests**: [src/test/java/tests](src/test/java/tests) — test classes (e.g., `LoginTests`).
- **Page objects**: [src/test/java/pages](src/test/java/pages).
- **Base test**: [src/test/java/base/BaseTest.java](src/test/java/base/BaseTest.java).
- **Config**: [src/main/resources/config.properties](src/main/resources/config.properties).
- **Screenshots**: `screenshots/`.
- **Allure results**: `allure-results/`.

Test Structure & Conventions
- Pattern: Page Object Model — UI interactions in page classes; tests orchestrate flows.
- Tests are atomic and independent; each `@Test` should start from a known state.
- Use explicit assertions for both positive and negative cases.
- Store credentials in `config.properties`; do not hardcode secrets.
- Page methods may return `this` for fluent chaining where appropriate.

How to Add Tests
- Add a TestNG class under `src/test/java/tests`.
- Use the page objects in `src/test/java/pages` to interact with the UI.
- Prefer explicit waits in page methods and resilient locators.
- Use `@DataProvider` for data-driven scenarios.

Utilities
- `DriverFactory`: obtains WebDriver instances.
- `TestListeners`: captures screenshots and test lifecycle hooks.
- `ConfigReader`: centralized access to `config.properties`.

Security & Reporting Checks
- Error messages: tests assert user-friendly/generic messages (no stacktraces, internal exceptions, tokens, or sensitive data).
- Cookies: tests check `HttpOnly` and `Secure` flags.
- Session handling: tests verify session reuse is prevented after logout.
- Injection/XSS: tests send malicious payloads and assert sanitization.




