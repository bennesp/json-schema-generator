# Json Schema Generator

This is a simple API to generate json schema from data.

## Known integrations

Since this is a very generic API, it can be exploited in many ways.

Here I report the major integrations I am aware of. Open an issue if you wish to contribute to this list.

### VSCode YAML extension
If you have the [YAML extension](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-yaml) installed, then
you can integrate this in vscode.

To provide json schema completion just put a comment similar to the following at the beginning of a yaml file:

```yaml
# yaml-language-server: $schema=https://jsg.arma.bennes.it?url=https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/crds/application-crd.yaml
```

After that, you can use the auto-completion as usual:

![vscode auto-completion yaml](./docs/assets/vscode-yaml-autocomplete.png)

## Parameters

| Parameter  | Default | Optional | Description                                                                                                        |
|------------|---------|----------|--------------------------------------------------------------------------------------------------------------------|
| `data`     | -       | no       | The data you want to generate a schema for.<br/>Note: use either this or `url`                                     |
| `url`      | -       | no       | A URL where your data to generate a schema for is returned upon a GET request.<br/>Note: use either this or `data` |
| `encoding` | nop     | yes      | A supported encoding to use to decode `data` or `url` content. Can be `nop`, `base64` or `hex`                     |
| `input`    | yaml    | yes      | A supported format to use to parse the input `data` or `url` content. Can be `yaml` or `json`                      |
| `output`   | json    | yes      | A supported format to use to serialize the schema generated. Can be `yaml` or `json`                               |

## API description

Data can be passed to the API in a couple of ways:

- As a data string, with the query parameter `data`
- As an url, with the query parameter `url`

**Note**: only one and exactly one of the two parameters must be used at a time

Moreover, you can specify a supported encoding to parse both the `data` or the `url` content,
with the query parameter `encoding`.
Supported encoders are:

- `nop` (default): just returns the data as is
- `base64`: decodes the data using base64
- `hex`: decodes the data using hexadecimal (eg: '41424344' is decoded as 'ABCD')

Moreover, you can specify a supported format to parse the input and to serialize the
output for both the `data` or the `url` content, with the query parameters
`input` and `output`.
Supported formats are:

- `yaml` (default for input): parses the data as yaml
- `json` (default for output): parses the data as json
