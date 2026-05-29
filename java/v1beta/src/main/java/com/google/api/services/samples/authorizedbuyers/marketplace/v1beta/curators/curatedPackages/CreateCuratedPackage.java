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
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.AccessControlSettings;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.CuratedPackage;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.Money;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.PackageTargeting;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class CreateCuratedPackage {

  /**
   * Executes the create operation for a curated package.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param parent the parent resource name under which the curated package will be created.
   * @param newCuratedPackage the CuratedPackage object to create.
   * @throws IOException if the API returns an error.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient,
      String parent,
      CuratedPackage newCuratedPackage)
      throws IOException {
    // [START sample_execute]
    System.out.printf("Creating Curated Package \"%s\" for curator with name \"%s\".%n",
        newCuratedPackage.getDisplayName(), parent);

    // Create a new curated package.
    CuratedPackage createdPackage =
        marketplaceClient
            .curators()
            .curatedPackages()
            .create(parent, newCuratedPackage)
            .execute();

    System.out.println("Successfully created curated package:");
    Utils.jsonPrettyPrint(createdPackage);
    // [END sample_execute]
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreateCuratedPackage")
            .build()
            .defaultHelp(true)
            .description("Creates a new curated package for the given curator account.");

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that will create the curated package.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--display_name")
        .help("The display name of the curated package to be created.")
        .required(true);
    parser
        .addArgument("--allowed_media_planners")
        .help("The space-delimited resource names of media planners you want to grant access " +
            "to the curated package. Eligible media planners can be found use the " +
            "mediaPlanners.list method. To learn more, see: " +
            "https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1beta/mediaPlanners/list")
        .required(true)
        .nargs("+");
    parser
        .addArgument("--included_data_segments")
        .help("The space-delimited resource names of the data segments you want to target. " +
            "You can find data segments for your account using the curators.dataSegments.list " +
            "method. To learn more, see:" +
            "https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1beta/curators.dataSegments/list")
        .required(true)
        .nargs("+");
    parser
        .addArgument("-c", "--fee_currency_code")
        .help("The three-letter currency code defined in ISO 4217. For example, \"USD\".")
        .required(true);
    parser
        .addArgument("-u", "--fee_units")
        .help("The whole units of your CPM fee in the specified currency. For example, if " +
            "`currencyCode` were set to \"USD\", a value of \"1\" would be $1 USD for 1,000 " +
            "views.")
        .type(Long.class)
        .required(true);
    parser
        .addArgument("-n", "--fee_nanos")
        .help("The nano units of your CPM fee, representing a fraction of the specified " +
            "currency. For example, if `currencyCode` were set to \"USD\", a value of " +
            "\"20000000\" would be $0.02 USD for 1,000 views.")
        .type(Integer.class)
        .required(true);

    // Optional arguments.
    parser
        .addArgument("--description")
        .help("The description of the curated package.");

    return parser;
  }

  // [START sample_build_resource_data_model]
  /**
   * Builds a CuratedPackage object from the parsed command-line arguments.
   *
   * @param parsedArgs the parsed command-line arguments.
   * @return the constructed CuratedPackage object.
   */
  private static CuratedPackage buildCuratedPackage(Namespace parsedArgs) {
    String displayName = parsedArgs.getString("display_name");
    String description = parsedArgs.getString("description");
    List<String> allowedMediaPlanners = parsedArgs.getList("allowed_media_planners");
    List<String> includedDataSegments = parsedArgs.getList("included_data_segments");
    String feeCurrencyCode = parsedArgs.getString("fee_currency_code");
    Long feeUnits = parsedArgs.getLong("fee_units");
    Integer feeNanos = parsedArgs.getInt("fee_nanos");

    CuratedPackage newCuratedPackage = new CuratedPackage()
        .setDisplayName(displayName)
        .setDescription(description);

    newCuratedPackage.setAccessSettings(
        new AccessControlSettings()
            .setAllowlistedMediaPlanners(allowedMediaPlanners));

    PackageTargeting packageTargeting = new PackageTargeting()
        .setIncludedDataSegments(includedDataSegments);

    newCuratedPackage.setTargeting(packageTargeting);

    Money feeCpm = new Money()
        .setCurrencyCode(feeCurrencyCode)
        .setUnits(feeUnits)
        .setNanos(feeNanos);

    newCuratedPackage.setFeeCpm(feeCpm);

    return newCuratedPackage;
  }
  // [END sample_build_resource_data_model]

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

    Long accountId = parsedArgs.getLong("account_id");
    String parent = String.format("curators/%s", accountId);
    CuratedPackage newCuratedPackage = buildCuratedPackage(parsedArgs);

    try {
      execute(client, parent, newCuratedPackage);
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }
  }
}
