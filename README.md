# bytemsg233-lib-java

Java runtime helpers for bytemsg233 generated code.

## Install

```bash
bytemsg233 install-lib java --to ./libs/bytemsg233
```

The package namespace is `com.neko233.bytemsg233`.

Runtime encode/decode and pool helpers are single-threaded. Pools use `ArrayDeque`; no `java.util.concurrent`, `synchronized`, thread pool, or background worker is used.
