# Authorized Buyers Marketplace API Java Samples

These samples demonstrate basic usage of the Authorized Buyers Marketplace API.

The Authorized Buyers Marketplace API Java Client Library makes it easier to
write Java clients to programmatically access Marketplace on behalf of specified
Authorized Buyers accounts. The reference documentation for the Authorized
Buyers Marketplace API is available from
<https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest>.

## Prerequisites

- [`Java 8+`](http://java.com)
- [`Maven`](http://maven.apache.org)

## Announcements and updates

For API and client library updates and news, see the following resources:

- [Marketplace API release notes](https://developers.google.com/authorized-buyers/apis/relnotes#marketplace-api)
- [Google Ads Developer Blog](http://googleadsdeveloper.blogspot.com/)

## Running the examples

### Download the repository contents

To download the contents of the repository, you can use the command

```
git clone https://github.com/googleads/authorized-buyers-marketplace-api-samples.git
```

or browse to <https://github.com/googleads/authorized-buyers-marketplace-api-samples>
and download a zip.

### Getting started

This sample uses the OAuth 2.0 Service Account flow for security, which is the
recommended workflow when working with the Marketplace API. You can learn
more about OAuth 2.0 and alternative workflows at:
<https://developers.google.com/identity/protocols/oauth2>

If you don't already have a Service Account and corresponding JSON key file

 * Launch the Google Cloud Console <https://console.cloud.google.com/apis/dashboard>
 * Access the menu and click the **Enabled APIs and Services** option, and then
   the **Credentials** tab. From here, you can either click the
   **Create credentials** button to create a new Service Account for use with
   this project, or find an existing one under **Manage service accounts**.
 * In the `Manage service accounts` page, you can generate a new key for the
   Service Account by clicking the ellipses in the proper row and selecting
   `Create key` in the resulting menu. Select `JSON` as the `Key type`. Click
   the `create` button and the file will be generated and downloaded to your
   computer. Place the JSON file in the sample directory. You will need the
   Service Account email on this page for the next step.
 * To authorize a Service Account to access your Authorized Buyers account via
   the API, go to the
   [Authorized Buyers UI](https://realtimebidding.google.com/).
   and click the `Admin` drop-down menu in the in the navigation bar corner, and
   then click the `Account users` option. Click the `Link Service Account`
   button on this page and enter the Service Account's email to associate it
   with your Authorized Buyers Account.
 * Navigate to the **Utils.java** file for the API version you are using, and
   update the `JSON_FILE` field to represent the path to the JSON key file you
   downloaded earlier. If you placed it in the sample directory, this should be
   the filename.
 * You must specify all of the required command-line arguments to run any of
   the samples. Required and optional arguments are a subset of resource fields
   and path parameters used to construct an API request, and are not
   exhaustive. Review the sample source code to see the supported command-line
   arguments.

## Setup the environment

### Via the command line

Execute the following command:

```bash
$ mvn compile
```

**Note:** IDEs such as [IntelliJ IDEA](https://www.jetbrains.com/idea/)
automatically handle importing Maven dependencies and compilation.

## Running the Examples

If you are running the examples via the command line, you can run individual
examples by navigating to the directory for your desired version and running a
command such as the following:

```bash
mvn exec:java -Dexec.mainClass=<CLASSPATH_TO_EXAMPLE> \
-Dexec.args="<INSERT_ARGUMENTS_HERE>"
```

For example, running the buyers ListClients example would look like:

```bash
mvn exec:java -Dexec.mainClass=com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.clients.ListClients \
-Dexec.args="--account_id <INSERT_ACCOUNT_ID>>"
```
