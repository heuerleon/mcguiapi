[![JitPack](https://jitpack.io/v/heuerleon/mcguiapi.svg)](https://jitpack.io/#heuerleon/mcguiapi)
![License](https://img.shields.io:/github/license/heuerleon/mcguiapi)
[![CodeQL](https://github.com/heuerleon/mcguiapi/actions/workflows/codeql-analysis.yml/badge.svg?branch=master)](https://github.com/heuerleon/mcguiapi/actions/workflows/codeql-analysis.yml)
[![Build](https://github.com/heuerleon/mcguiapi/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/heuerleon/mcguiapi/actions/workflows/build.yml)
[![pages-build-deployment](https://github.com/heuerleon/mcguiapi/actions/workflows/pages/pages-build-deployment/badge.svg?branch=master)](https://github.com/heuerleon/mcguiapi/actions/workflows/pages/pages-build-deployment)
# MC GUI API
MC GUI API is an API for creating graphical user interfaces using inventories in minecraft.
- [Wiki](https://github.com/heuerleon/mcguiapi/wiki)
- [Docs](https://heuerleon.github.io/mcguiapi/)

## Adding MC GUI API to your dependencies
If you use Maven:
```xml
<repositories>
  <repository>
    <id>jitpack</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.heuerleon</groupId>
    <artifactId>mcguiapi</artifactId>
    <version>v1.3.3</version>
  </dependency>
</dependencies>
```

If you use Gradle:
```groovy
repositories {
  maven {
    url 'https://jitpack.io'
  }
}

dependencies {
  implementation 'com.github.heuerleon:mcguiapi:v1.3.3'
}
```

Do not forget to shadow the dependency into your jar. This is not a standalone Minecraft plugin and you will get errors otherwise.
