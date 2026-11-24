# TASK A4 – What Should Be Serialized?

## A4.1 Decide what to persist
Mark the live database `Connection` field `transient`; it is a runtime resource and is not serializable or meaningful after the program restarts. Mark the password `transient` as well, because credentials should not be stored in a serialized application-state file. Prefer not to persist plaintext passwords at all; store secure password hashes in a protected database. Persist resident name and register number only if required by the application.

When `Hostel` is serialized, Java follows references from the root object and serializes the reachable object graph: its rooms and the residents in those rooms, unless a field is `static` or `transient`. Every reachable non-transient object must be serializable.

## A4.2 Plan for change
When a new `phoneNumber` field is added, an older serialized stream does not contain a value for it. With a compatible, explicitly declared `serialVersionUID`, Java can usually deserialize the old object and the new field receives its default value (`null` for a String). If the class's computed UID changes and no matching UID exists in the stream, deserialization throws `InvalidClassException`. Declare a stable `private static final long serialVersionUID = 1L;` and implement migration/default handling where needed.

## Alternative: CSV or JSON
CSV/JSON are text formats and are easier to inspect and exchange; they require explicit conversion code or a library. Java serialization is convenient for object graphs but is Java-specific and sensitive to class-version changes. Saving/loading n residents is O(n) time for all approaches. File size depends on object metadata and encoding; Java serialization often adds class/type metadata, while compact CSV can be smaller, though JSON field names add overhead. Adding a field to CSV/JSON can be handled by optional columns/properties and default values, provided the parser is designed for schema evolution. Serialization needs compatible class versioning and migration logic.
