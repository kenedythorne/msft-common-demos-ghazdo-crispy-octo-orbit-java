# Overview

There is no need to call `toString` on a `String` because it just returns the object itself. From the Java API Specification entry for `String.toString()`:

> `public String toString()`  
> This object (which is already a string!) is itself returned.

# Recommendation

Do not call `toString` on a `String` object.

# Example

The following example shows an unnecessary call to `toString` on the string `name`.

```java
// CallsToStringToString.java
String name = "example";
System.out.println(name.toString());
