# JVesta

## What is JVesta?

_JVesta_ is a small Java library simplifying access to a Vestaboard via its Local API. It provides
templating and formatting facilities along with an abstracted means of invoking the API
to display a message on a flagship Vestaboard.

This project is in no way endorsed by Vestaboard. Users of it do so at their own risk.

## Who is it for?

It is intended to be used by those wishing to develop custom Vestaboard integrations in Java,
running on the same local network as a Vestaboard.

## Key classes

There are three key classes in the _JVesta_ library: `VestaMessagePayload`, `MessageWriter` and
`VestaLocalApi`.

### `VestaMessagePayload`

This class is an abstraction of the data you wish to display on your Vestaboard. You instantiate it using its default constructor:

```java
var payload = new VestaMessagePayload();
```

which represents the matrix of 22x6 Bits each initialised to the blank character.

### `MessageWriter`

Once you have a payload, you'll want to start adding content to it. You can
do this incrementally using the `MessageWriter` class which provides a number
of templating and formatting features. Nothing will be sent to the Vestaboard
until you invoke the API: see the next section for how to do that.

The class uses a builder pattern that allows you to specify the
properties of the builder in a optional and chained fashion. An example invocation is:

```java
var writer = new MessageWriter()
    .line(1)
    .alignment(HorizontalAlignment.CENTER)
    .template("hello {{name}}")
    .parameters(Map.of("name", "samantha"));
```

In the above example:

* `line` is a 0-based index of the line on the Vestaboard to update, the default being 0. This library
only supports single-line updates at a time: there is no automatic or explicit wrapping
* `alignment` provides alignment information: `LEFT` is default, with `CENTER` and `RIGHT` being the other options
* `template` is the all-important string to display. As its name suggests, it supports variable injection through the use of the `parameters` and `parameter` methods: see the next section for details

Truncation to the 22-Bit width will automatically occur. Some examples of alignment are shown below, using underscores instead of spaces for clarity: 

| Template | Alignment | Result |
|---|---|---|
| `hello world` | LEFT   | `HELLO WORLD___________` |
| `hello world` | CENTER | `_____HELLO WORLD______` |
| `hello world` | RIGHT  | `___________HELLO WORLD` |
| `hello world and universe` | LEFT   | `HELLO WORLD AND UNIVER` |
| `hello world and universe` | CENTER | `ELLO WORLD AND UNIVERS` |
| `hello world and universe` | RIGHT  | `LLO WORLD AND UNIVERSE` |

Vestaboard supports a [limited character set](https://docs.vestaboard.com/docs/characterCodes). All alphabetic characters will be automatically upper-cased and
any unsupported characters in the template will be replaced with the blank character.

Aside from variable substitution which you'll learn more about shortly, you can
render one of Vestaboard's 8 colour blocks using the `{x}` syntax, where `x` can be one of: `r`, `o`, `y`, `g`, `b`, `v`, `w`, and `k` specifying red, orange, yellow, green, blue, violet, white and black respectively.

Once you have built your writer, it can be applied to a payload thus:

```java
writer.writeTo(payload);
```

You can instantiate multiple builders and apply each of them to the payload to
build up the combined payload before you send it to the Vestaboard. Be aware that
a writer can overwrite a previous writer's actions if their bounds overlap.

#### Templating, formatting and truncation

A writer's payload can just be text that you've already formatted in your
application code, if that's the way you wish to build your integration.

However, _JVesta_ provides some helpers to simplify this process, if you wish.
It has taken some inspiration from Vestabaord's own [VBML](https://docs.vestaboard.com/docs/vbml) language whilst providing a simpler, more Java-native approach.

If your template includes strings that look like `{{xxx}}` then the writer will
treat these as variable substitutions and will attempt to resolve them.

Variable values can be provided to the writer either through a number of individual assignments or as a one-off assignment. This code:

```java
new MessageWriter()
    .template("{{name}} has turned {{age}}")
    .parameter("name", "samantha")
    .parameter("age", 28);
```

is equivalent to this:

```java
new MessageWriter()
    .template("{{name}} has turned {{age}}")
    .parameters(Map.of(
        "name", "samantha",
        "age", 28
    ))
```

Note that the case of the templated variable and the parameter key must match. Mismatched or missing variables
will simply be ignored and replaced with the empty string.

Whilst a rendered template can be aligned left, right or centered, individual
components of a template can also be aligned and truncated through an extended
variable substitution syntax. 

The table below shows examples of this when a `name` of `samantha` has been provided.
Note that underscores again represent spaces for clarity:

| Template | Meaning | Result |
|---|---|---|
| `{{name,-12}}` | Left-justify to a width of 12   | `SAMANTHA____` |
| `{{name,12}}`  | Right-justify to a width of 12  | `____SAMANTHA` |
| `{{name,=12}}` | Center-justify to a width of 12 | `__SAMANTHA__` |
| `{{name,-3}}`  | Left-justify to a width of 3, truncating    | `SAM` |
| `{{name,3}}`   | Right-justify to a width of 3, truncating   | `THA` |
| `{{name,=3}}`  | Center-justify to a width of 3, truncating  | `MAN` |

There is one further formatting option available to you, and that is the
standard Java string formatting. Given a `price` of `297`:

| Template | Meaning | Result |
|---|---|---|
| `{{price,%7.2f}}` | Float right-justified with width 7 and precision 2 | `_297.00` |

### `VestaLocalApi`

This class abstracts out the actual HTTP call to the Vestaboard's local API.

You need to enable local API access on your Vestaboard before you can start
using it: details can be found [here](https://docs.vestaboard.com/docs/local-api/authentication). Once you have enabled access, you will have in your possession an API token.

With this, you can construct a `VestaLocalApi` by providing the constructor with
the network address of your Vestaboard and the API token:

```java
var api = new VestaLocalApi("vestabaord.local:7000", "my-api-key");
```

There is a convenience method that allows you to connect to a server on your local
machine, for example if you are running a local simulator such as [Vestasim](https://github.com/hollingl/vestasim):

```java
var api =  VestaLocalApi.localhost();
```

When your code wants to update the Vestaboard, simply call the `setMessage` method:

```java
var success = api.setMessage(payload);
```

And that's it, your Vestaboard will shortly display your payload.
