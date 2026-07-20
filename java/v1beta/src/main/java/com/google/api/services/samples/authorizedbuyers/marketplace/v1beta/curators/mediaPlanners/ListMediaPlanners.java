/*
 * Copyright (c) 2026 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
express
 * or implied. See the License for the specific language governing permissions and limitations
under
 * the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.curators.mediaPlanners;

import com.google.api.services.authorizedbuyersmarketplace.v1beta.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.ListMediaPlannersResponse;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.Money;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class ListMediaPlanners {

  private ListMediaPlanners() {}

  /**
   * Lists all media planner accounts that the caller has access to. For curators, this will return
   * all media planners that have accepted curator terms. For other accounts, attempting to list
   * media planners will return an error.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param parent the parent resource name under which the data segments will be listed.
   * @param pageSize the number of rows to return per page.
   * @throws IOException if the API returns an error.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient, Integer pageSize, String filter)
      throws IOException {

    String pageToken = null;

    // Iterate through and print pages from the media planners list.
    do {
      ListMediaPlannersResponse response =
          marketplaceClient
              .mediaPlanners()
              .list()
              .setFilter(filter)
              .setPageSize(pageSize)
              .setPageToken(pageToken)
              .execute();
      Utils.jsonPrettyPrint(response);
      pageToken = response.getNextPageToken();
    } while (pageToken != null);
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListMediaPlanners")
            .build()
            .defaultHelp(true)
            .description("Lists all media planner accounts that the caller has access to.");

    // Optional arguments.
    parser
        .addArgument("-d", "--page_size")
        .help(
            "The number of rows to return per page. The server may return fewer rows than "
                + "specified.")
        .type(Integer.class);
    parser
        .addArgument("-f", "--filter")
        .help(
            "An optional parameter used the filter the media planners returned. Uses Cloud API list"
                + " filtering syntax. To learn more, see:"
                + "https://developers.google.com/authorized-buyers/apis/guides/get-started/list-filters");

    return parser;
  }

  public static void main(String[] args) {
    ArgumentParser parser = createArgumentParser();
    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException e) {
      parser.handleError(e);
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

    Integer pageSize = parsedArgs.getInt("page_size");
    String filter = parsedArgs.getString("filter");

    try {
      execute(client, pageSize, filter);
    } catch (Exception e) {
      System.out.printf("Marketplace API returned error response:%n%s", e);
      System.exit(1);
    }
  }
}
