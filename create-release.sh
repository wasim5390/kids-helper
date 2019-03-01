#!/usr/bin/env bash
set -e

PLAYKEYFILE_LOCATION='/Users/home/.ssh/Google_Play_Android_Developer-04b371af6b8a.p12'
KEYFILE_LOCATION='/Users/home/Documents/workspace-uiu/Companion/keystore.jks'
VERSION_CODE="35"
VERSION_NAME="n1.0.35"

file="$HOME/companion.properties"

if [ -f "$file" ]
then
  echo "$file found."
  . $file
else
  echo "$file not found."
fi

echo $KEY_PASSWORD
echo $STORE_PASSWORD

# ensure git it up to date.
git pull

gradle \
    -Dorg.gradle.jvmargs=-Xmx4608M \
    -PkeyPasswordProperty="$KEY_PASSWORD" \
    -PstorePasswordProperty="$STORE_PASSWORD" \
    -PkeystoreFileLocation="$KEYFILE_LOCATION" \
    -PversionCodeProperty="$VERSION_CODE" \
    -PversionNameProperty="$VERSION_NAME" \
    -PplayKeyFileLocation="$PLAYKEYFILE_LOCATION" \
test assembleRelease publishApkRelease

# tag the git repo with the version code
echo "Tag git with label $VERSION_NAME"
git tag -a $VERSION_NAME -m "Automated tag for $VERSION_NAME and $VERSION_CODE"

echo "push new tag to origin"
git push origin --tags

echo "Helper release $VERSION_CODE / $VERSION_NAME"

