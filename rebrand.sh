#!/bin/bash
set -e

BACKEND_SRC="eazystore/src/main/java"
TEST_SRC="eazystore/src/test/java"
OLD_PKG_PATH="com/eazybytes/eazystore"
NEW_PKG_PATH="com/jaykabra/jaykart"
OLD_PKG="com.eazybytes.eazystore"
NEW_PKG="com.jaykabra.jaykart"

echo "========================================"
echo "  JayKart Rebranding Script"
echo "========================================"

# ─────────────────────────────────────────────────
# STEP 1: Move all Java source files to new package path
# ─────────────────────────────────────────────────
echo "[1/7] Creating new package directory structure..."

OLD_DIR="$BACKEND_SRC/$OLD_PKG_PATH"
NEW_DIR="$BACKEND_SRC/$NEW_PKG_PATH"

mkdir -p "$NEW_DIR"

# Move all subdirectories
for subdir in "$OLD_DIR"/*/; do
  dirname=$(basename "$subdir")
  mkdir -p "$NEW_DIR/$dirname"
  cp -r "$subdir"* "$NEW_DIR/$dirname/" 2>/dev/null || true
done

# Move the main application file
cp "$OLD_DIR/EazystoreApplication.java" "$NEW_DIR/JayKartApplication.java"

echo "[1/7] Done."

# ─────────────────────────────────────────────────
# STEP 2: Move test files
# ─────────────────────────────────────────────────
echo "[2/7] Moving test files..."
OLD_TEST_DIR="$TEST_SRC/$OLD_PKG_PATH"
NEW_TEST_DIR="$TEST_SRC/$NEW_PKG_PATH"
mkdir -p "$NEW_TEST_DIR"
cp "$OLD_TEST_DIR/EazystoreApplicationTests.java" "$NEW_TEST_DIR/JayKartApplicationTests.java" 2>/dev/null || true
echo "[2/7] Done."

# ─────────────────────────────────────────────────
# STEP 3: Replace all package/import declarations inside new Java files
# ─────────────────────────────────────────────────
echo "[3/7] Replacing package names in all Java source files..."

find "$NEW_DIR" -name "*.java" -print0 | xargs -0 sed -i '' \
  -e "s|$OLD_PKG|$NEW_PKG|g" \
  -e "s|EazystoreApplication|JayKartApplication|g"

# Fix main application file specifically
sed -i '' \
  -e "s|$OLD_PKG|$NEW_PKG|g" \
  -e "s|EazystoreApplication|JayKartApplication|g" \
  "$NEW_DIR/JayKartApplication.java"

# Fix test file
sed -i '' \
  -e "s|$OLD_PKG|$NEW_PKG|g" \
  -e "s|EazystoreApplicationTests|JayKartApplicationTests|g" \
  "$NEW_TEST_DIR/JayKartApplicationTests.java" 2>/dev/null || true

echo "[3/7] Done."

# ─────────────────────────────────────────────────
# STEP 4: Rename security class
# ─────────────────────────────────────────────────
echo "[4/7] Renaming security class..."
if [ -f "$NEW_DIR/security/EazyStoreNonProdUsernamePwdAuthenticationProvider.java" ]; then
  mv "$NEW_DIR/security/EazyStoreNonProdUsernamePwdAuthenticationProvider.java" \
     "$NEW_DIR/security/JayKartNonProdAuthProvider.java"
  sed -i '' \
    -e "s|EazyStoreNonProdUsernamePwdAuthenticationProvider|JayKartNonProdAuthProvider|g" \
    "$NEW_DIR/security/JayKartNonProdAuthProvider.java"
  # Update any references to this class in other files
  find "$NEW_DIR" -name "*.java" -print0 | xargs -0 sed -i '' \
    -e "s|EazyStoreNonProdUsernamePwdAuthenticationProvider|JayKartNonProdAuthProvider|g"
fi
echo "[4/7] Done."

# ─────────────────────────────────────────────────
# STEP 5: Update pom.xml
# ─────────────────────────────────────────────────
echo "[5/7] Updating pom.xml..."
sed -i '' \
  -e 's|<groupId>com.eazybytes</groupId>|<groupId>com.jaykabra</groupId>|g' \
  -e 's|<artifactId>eazystore</artifactId>|<artifactId>jaykart</artifactId>|g' \
  -e 's|<name>eazystore</name>|<name>jaykart</name>|g' \
  -e 's|<description>Backend Project for Eazy Store E-commerce application</description>|<description>JayKart – Full-Stack E-Commerce Application built by Jay Kabra</description>|g' \
  -e 's|<developer/>|<developer><id>maheshwarijay2005</id><name>Jay Kabra</name><email>jay.kabra@example.com</email><url>https://github.com/maheshwarijay2005</url><roles><role>developer</role></roles></developer>|g' \
  eazystore/pom.xml
echo "[5/7] Done."

# ─────────────────────────────────────────────────
# STEP 6: Update application.properties files
# ─────────────────────────────────────────────────
echo "[6/7] Updating properties files..."
sed -i '' \
  -e 's|spring.application.name=eazystore|spring.application.name=jaykart|g' \
  -e 's|eazystore/actuator|jaykart/actuator|g' \
  -e 's|Backend Project for Eazy Store E-commerce application|JayKart – Full-Stack E-Commerce Application|g' \
  -e 's|contact.email=dev@eazystore.com|contact.email=jay.kabra@example.com|g' \
  -e 's|eazystore}/eazystore|jaykart}/jaykart|g' \
  eazystore/src/main/resources/application.properties

sed -i '' \
  -e 's|eazybytes|kabra_jay|g' \
  -e 's|eazystore|jaykart|g' \
  eazystore/src/main/resources/application-qa.properties

sed -i '' \
  -e 's|eazybytes|kabra_jay|g' \
  -e 's|eazystore|jaykart|g' \
  eazystore/src/main/resources/application-prod.properties

echo "[6/7] Done."

# ─────────────────────────────────────────────────
# STEP 7: Update Frontend
# ─────────────────────────────────────────────────
echo "[7/7] Updating frontend branding..."

# package.json
sed -i '' 's|"eazystore-ui"|"jaykart-ui"|g' eazystore-ui/package.json

# index.html — title & meta
sed -i '' \
  -e 's|<title>Vite + React</title>|<title>JayKart – Online Shopping Store</title>|g' \
  -e 's|href="/vite.svg"|href="/cart-icon.svg"|g' \
  eazystore-ui/index.html

# Header branding
sed -i '' \
  's|Eazy Stickers|JayKart|g' \
  eazystore-ui/src/components/Header.jsx

# Home page title
sed -i '' \
  's|Explore Eazy Stickers!|Explore JayKart!|g' \
  eazystore-ui/src/components/Home.jsx

# Footer — remove eazybytes link
sed -i '' \
  -e 's|https://eazybytes.com/|https://github.com/maheshwarijay2005|g' \
  -e 's|eazybytes|Jay Kabra|g' \
  eazystore-ui/src/components/footer/Footer.jsx

# About — remove eazybytes reference
sed -i '' \
  's|Designs by eazybytes|Developed by Jay Kabra|g' \
  eazystore-ui/src/components/About.jsx

echo "[7/7] Done."

echo ""
echo "========================================"
echo "  ✅ Rebranding Complete!"
echo "  Old: com.eazybytes.eazystore (EazyStore)"
echo "  New: com.jaykabra.jaykart (JayKart)"
echo "========================================"
echo ""
echo "IMPORTANT: Delete the old package directory from IntelliJ"
echo "  -> eazystore/src/main/java/com/eazybytes/"
echo ""
