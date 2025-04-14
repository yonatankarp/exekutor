#!/bin/bash

# Script to initialise project by executing steps as follows:
#   - Replace port number
#   - Replace package `spring-starter`
#   - Replace app name from `spring-starter`
#   - Clean-up README file from template related info
#   - Self-destruct

read -p "Port number for new app (press Enter for default 8080): " port
if [ -z "$port" ]
then
      port="8080"
fi

read -p "Replace application name with: " component_name
read -p "Replace \`spring-starter\` package name with: " package

pushd "$(dirname "$0")"/.. > /dev/null || exit 1

declare -a files_with_port=(
  "README.md"
  "./spring-starter/Dockerfile"
  "./spring-starter/src/main/resources/application.yml"
)

declare -a files_with_slug=(
  "build.gradle.kts"
  "settings.gradle.kts"
  "README.md"
  "docker-compose.yml"
  "./buildSrc/src/main/groovy/spring-starter.java-conventions.gradle"
  "./buildSrc/src/main/groovy/spring-starter.publishing-conventions.gradle"
  "./spring-starter/build.gradle.kts"
  "./spring-starter/Dockerfile"
  "./spring-starter/src/main/resources/application.yml"
  "./spring-starter/src/main/kotlin/com/yonatankarp/spring/starter/adapters/input/http/rest/HelloWorldHttpAdapter.kt"
  ".github/workflows/build.yml"
)

declare -a subdirectories_to_rename_package=(
  "main"
  "test"
)

declare -a headers_to_delete=(
  "Purpose"
  "What's inside"
  "Setup"
)


# Replace port number
for i in "${files_with_port[@]}"
do
  perl -i -pe "s/8080/$port/g" "${i}"
done

# Replace spring-boot-template slug
for i in "${files_with_slug[@]}"
do
  perl -i -pe "s/spring-starter/$component_name/g" "${i}"
done

# Replace OpenApi package name in Gradle plugin
perl -i -pe "s/karp.spring.starter/karp.$package/g" spring-starter/build.gradle.kts

# Replace demo package in all files under ./src
find ./spring-starter/src -type f -print0 | xargs -0 perl -i -pe "s/karp.spring.starter/karp.$package/g"
perl -i -pe "s/karp.spring.starter/karp.$package/g" build.gradle.kts

# Rename directory to provided package name

rm -rf "${component_name}"

for dir in "${subdirectories_to_rename_package[@]}"
do
  git mv "./spring-starter/src/${dir}/kotlin/com/yonatankarp/spring/starter" "./spring-starter/src/${dir}/kotlin/com/yonatankarp/${package}"
done

git mv "./spring-starter" "${component_name}"

# Rename buildSrc files

find . -type f -name "spring-starter*" -not -path "*buildSrc/build/*" |
sed 's/\(.*\)\(spring-starter\)\(.*\)/git mv "\1\2\3" "\1'"${component_name}"'\3"/g' |
sh

# Clean-up README file
for i in "${headers_to_delete[@]}"
do
  perl -0777 -i -p0e "s/## $i.+?\n(## )/\$1/s" README.md
done

# Rename title to slug
perl -i -pe "s/.*\n/# $component_name\n/g if 1 .. 1" README.md

# Self-destruct
echo  "Self destroy in 3... 2... 1..."
rm bin

# Return to original directory
popd > /dev/null || exit 1
