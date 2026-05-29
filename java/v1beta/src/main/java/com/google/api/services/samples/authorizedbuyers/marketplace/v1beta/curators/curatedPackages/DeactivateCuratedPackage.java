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
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.CuratedPackage;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.DeactivateCuratedPackageRequest;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class DeactivateCuratedPackage {

  /**
   * Executes the deactivate operation for a curated package.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param accountId the resource ID of the curator account.
   * @param curatedPackageId the resource ID of the curated package.
   * @throws IOException if the API returns an error.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient, Long accountId, String curatedPackageId)
      throws IOException {
    String name = String.format("curators/%s/curatedPackages/%s", accountId, curatedPackageId);

    // [START sample_execute]
    System.out.printf("Deactivating curated package with name \"%s\".%n", name);

    // Deactivate the specified curated package.
    CuratedPackage deactivatedPackage =
        marketplaceClient
            .curators()
            .curatedPackages()
            .deactivate(name, new DeactivateCuratedPackageRequest())
            .execute();

    System.out.println("Successfully deactivated curated package:");
    Utils.jsonPrettyPrint(deactivatedPackage);
    // [END sample_execute]
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("DeactivateCuratedPackage")
            .build()
            .defaultHelp(true)
            .description("Deactivates a specific curated package.");

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that created the curated package.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-c", "--curated_package_id")
        .help("The resource ID of the curated package to deactivate.")
        .required(true);

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
          parsedArgs.getString("curated_package_id"));
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }
  }
}
