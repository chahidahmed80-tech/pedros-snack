const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_789";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// Products & Public Catalog
test("Public: anyone can read products", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertSucceeds(unauthDb.collection("products").get());
});

test("Non-admin: cannot create product", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com", email_verified: true }).firestore();
  await assertFails(
    aliceDb.collection("products").doc("burger_1").set({
      name: "L'Original",
      category: "Burgers",
      price: 37,
      isAvailable: true,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("Admin: can create product", async () => {
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, {
    email: "chahidahmed80@gmail.com",
    email_verified: true,
  }).firestore();
  await assertSucceeds(
    adminDb.collection("products").doc("burger_1").set({
      name: "L'Original",
      category: "Burgers",
      price: 37,
      isAvailable: true,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

// Orders Security
test("Unauthenticated: cannot create order", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("orders").doc("order_1").set({
      userId: ALICE_UID,
      orderNumber: "PEDRO-1001",
      customerName: "Alice",
      customerPhone: "+212600000000",
      orderType: "PICKUP",
      totalAmount: 120,
      status: "NEW",
      paymentMethod: "CASH_AT_PICKUP",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("Authenticated: Alice can create and read her own order", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com", email_verified: true }).firestore();
  await assertSucceeds(
    aliceDb.collection("orders").doc("order_alice_1").set({
      userId: ALICE_UID,
      orderNumber: "PEDRO-1001",
      customerName: "Alice",
      customerPhone: "+212600000000",
      orderType: "PICKUP",
      totalAmount: 120,
      status: "NEW",
      paymentMethod: "CASH_AT_PICKUP",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );

  await assertSucceeds(aliceDb.collection("orders").doc("order_alice_1").get());
});

test("Cross-user isolation: Bob cannot read Alice's order", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("orders").doc("order_alice_1").set({
      userId: ALICE_UID,
      orderNumber: "PEDRO-1001",
      customerName: "Alice",
      customerPhone: "+212600000000",
      orderType: "PICKUP",
      totalAmount: 120,
      status: "NEW",
      paymentMethod: "CASH_AT_PICKUP",
      createdAt: new Date(),
      updatedAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID, { email: "bob@test.com", email_verified: true }).firestore();
  await assertFails(bobDb.collection("orders").doc("order_alice_1").get());
});

// User Profile Security
test("User Profile: Alice can create her own profile with customer role", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com", email_verified: true }).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      email: "alice@test.com",
      displayName: "Alice",
      phone: "+212612345678",
      address: "Nahda, Khouribga",
      role: "customer",
      loyaltyPoints: 0,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("User Profile: Alice cannot self-assign owner role", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@test.com", email_verified: true }).firestore();
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      email: "alice@test.com",
      displayName: "Alice",
      role: "owner",
      loyaltyPoints: 100,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});
