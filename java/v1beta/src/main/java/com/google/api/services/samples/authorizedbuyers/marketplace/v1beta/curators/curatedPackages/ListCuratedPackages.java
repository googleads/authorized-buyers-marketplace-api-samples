/*
 * Copyright (c) 2026 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.curators.curatedPackages;

import com.google.api.services.authorizedbuyersmarketplace.v1beta.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.ListCuratedPackagesResponse;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class ListCuratedPackages {

  /**
   * Executes the list operation for curated packages.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param accountId the resource ID of the curator account.
   * @param pageSize the number of rows to return per page.
   * @param filter an optional parameter to filter the returned curated packages.
   * @throws IOException if the API returns an error.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient,
      Long accountId,
      Integer pageSize,
      String filter)
      throws IOException {
    String parentBuyerName = String.format("curators/%s", accountId);
    String pageToken = null;

    // [START sample_execute]
    if (filter == null) {
      System.out.printf("Listing curated packages for curator Account ID \"%d\":%n", accountId);
    } else {
      System.out.printf(
          "Listing curated packages for curator Account ID \"%d\" with filter \"%s\":%n",
          accountId,
          filter);
    }

    // Iterate through and print pages from the curatedPackages.list response.
    do {
      ListCuratedPackagesResponse response =
          marketplaceClient
              .curators()
              .curatedPackages()
              .list(parentBuyerName)
              .setFilter(filter)
              .setPageSize(pageSize)
              .setPageToken(pageToken)
              .execute();

      pageToken = response.getNextPageToken();
      Utils.jsonPrettyPrint(response);
    } while (pageToken != null);
    // [END sample_execute]
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListCuratedPackages")
            .build()
            .defaultHelp(true)
            .description(("Lists curated packages for the given curator account."));

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that created the curated package.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--page_size")
        .help("The number of rows to return per page. The server may return fewer rows than "
                + "specified.")
        .setDefault(Utils.getMaximumPageSize())
        .type(Integer.class);

    // Optional arguments.
    parser
        .addArgument("-f", "--filter")
        .help("An optional parameter used the filter the curated packages returned. Uses " +
            "Cloud API list filtering syntax. To learn more, see:" +
            "https://developers.google.com/authorized-buyers/apis/guides/get-started/list-filters");

    return parser;
  }

  public static void main(String[] args) {
    ArgumentParser parser = createArgumentParser();

    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException ex) {
      parser.handleError(ex);
      System.exit(1);
    }

    AuthorizedBuyersMarketplace client = null;
    try {
      client = Utils.getMarketplaceClient();
    } catch (IOException ex) {
      System.out.printf("Unable to create Marketplace API service:%n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport:%n%s", ex);
      System.exit(1);
    }

    try {
      execute(
          client,
          parsedArgs.getLong("account_id"),
          parsedArgs.getInt("page_size"),
          parsedArgs.getString("filter"));
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }
  }
}