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
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.Money;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.PackageTargeting;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class PatchCuratedPackage {

  /**
   * Executes the patch operation for a curated package.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param name the full resource name of the curated package to patch.
   * @param patchedPackage the CuratedPackage object containing the fields to update.
   * @param updateMask the comma-separated string representing the fields to be updated.
   * @throws IOException if the API returns an error.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient,
      String name,
      CuratedPackage patchedPackage,
      String updateMask)
      throws IOException {
    // [START sample_execute]
    CuratedPackage updatedPackage =
        marketplaceClient
            .curators()
            .curatedPackages()
            .patch(name, patchedPackage)
            .setUpdateMask(updateMask)
            .execute();

    System.out.printf("Curated package updated: %s%n", name);
    Utils.jsonPrettyPrint(updatedPackage);
    // [END sample_execute]
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchCuratedPackage")
            .build()
            .defaultHelp(true)
            .description("Updates a specific curated package. To modify the state of a" +
                "curated package, use the curators.curatedPackages.activate or " +
                "curators.curatedPackages.deactivate methods instead.");

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that created the curated package.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-c", "--curated_package_id")
        .help("The resource ID of the curated package to update.")
        .required(true);

    // Optional arguments.
    parser
        .addArgument("-d", "--display_name")
        .help("The modified value of `CuratedPackage.displayName`.");
    parser
        .addArgument("--description")
        .help("The modified value of `CuratedPackage.description`.");
    parser
        .addArgument("--floor_currency_code")
        .help("The modified value of `CuratedPackage.floorPriceCpm.currencyCode`. This must " +
            "be a three-letter currency code defined in ISO 4217. For example, \"USD\".");
    parser
        .addArgument("--floor_units")
        .help("The modified value of `CuratedPackage.floorPriceCpm.units`. This represents " +
            "whole units of the floor price in the specified currency. For example, if " +
            "`currencyCode` were set to \"USD\", a value of \"1\" would be $1 USD for 1,000 views.")
        .type(Long.class);
    parser
        .addArgument("--floor_nanos")
        .help("The modified value of `CuratedPackage.floorPriceCpm.nanos`. This represents " +
            "the nano units of the floor price in the specified currency. For example, if " +
            "`currencyCode` were set to \"USD\", a value of \"20000000\" would be $0.02 USD for " +
            "1,000 views.")
        .type(Integer.class);
    parser
        .addArgument("--included_data_segments")
        .help("A space-delimited list of modified values for " +
            "`CuratedPackage.targeting.includedDataSegments`. This replaces any previously " +
            "targeted data segments for the curated package. You must specify resource names of " +
            "the data segments you want to target. You can find data segments for your account " +
            "using the curators.dataSegments.list method. To learn more, see:" +
            "https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1beta/curators.dataSegments/list")
        .nargs("+");
    parser
        .addArgument("--fee_currency_code")
        .help("The modified value of `CuratedPackage.feeCpm.currencyCode`. This must be a " +
            "three-letter currency code defined in ISO 4217. For example, \"USD\".");
    parser
        .addArgument("--fee_units")
        .help("The modified value of `CuratedPackage.feeCpm.units.` This represents whole " +
            "units of your CPM fee in the specified currency. For example, if `currencyCode` " +
            "were set to \"USD\", a value of \"1\" would be $1 USD for 1,000 views.")
        .type(Long.class);
    parser
        .addArgument("--fee_nanos")
        .help("The modified value of `CuratedPackage.feeCpm.nanos`. The nano units of your " +
            "CPM fee, representing a fraction of the specified currency. For example, if " +
            "`currencyCode` were set to \"USD\", a value of \"20000000\" would be $0.02 USD for " +
            "1,000 views.")
        .type(Integer.class);

    return parser;
  }

  // [START sample_build_resource_data_model]
  /**
   * Populates the CuratedPackage object with values provided via command-line arguments
   * and builds the update mask indicating which fields were set.
   *
   * @param parsedArgs the parsed command-line arguments.
   * @param patchedPackage the CuratedPackage object to populate.
   * @return a comma-separated string representing the update mask.
   */
  private static String buildPatchedCuratedPackage(
      Namespace parsedArgs,
      CuratedPackage patchedPackage) {
    List<String> patchedFields = new ArrayList<>();

    String displayName = parsedArgs.getString("display_name");
    String description = parsedArgs.getString("description");
    String floorCurrencyCode = parsedArgs.getString("floor_currency_code");
    Long floorUnits = parsedArgs.getLong("floor_units");
    Integer floorNanos = parsedArgs.getInt("floor_nanos");
    List<String> includedDataSegments = parsedArgs.getList("included_data_segments");
    String feeCurrencyCode = parsedArgs.getString("fee_currency_code");
    Long feeUnits = parsedArgs.getLong("fee_units");
    Integer feeNanos = parsedArgs.getInt("fee_nanos");

    if (displayName != null) {
      patchedPackage.setDisplayName(displayName);
      patchedFields.add("displayName");
    }
    if (description != null) {
      patchedPackage.setDescription(description);
      patchedFields.add("description");
    }

    Money floorPriceCpm = new Money();
    if (floorCurrencyCode != null) {
      floorPriceCpm.setCurrencyCode(floorCurrencyCode);
      patchedFields.add("floorPriceCpm.currencyCode");
    }
    if (floorUnits != null) {
      floorPriceCpm.setUnits(floorUnits);
      patchedFields.add("floorPriceCpm.units");
    }
    if (floorNanos != null) {
      floorPriceCpm.setNanos(floorNanos);
      patchedFields.add("floorPriceCpm.nanos");
    }
    patchedPackage.setFloorPriceCpm(floorPriceCpm);

    if (includedDataSegments != null) {
      PackageTargeting packageTargeting = new PackageTargeting()
          .setIncludedDataSegments(includedDataSegments);
      patchedFields.add("targeting.includedDataSegments");
      patchedPackage.setTargeting(packageTargeting);
    }

    Money feeCpm = new Money();
    if (feeCurrencyCode != null) {
      feeCpm.setCurrencyCode(feeCurrencyCode);
      patchedFields.add("feeCpm.currencyCode");
    }
    if (feeUnits != null) {
      feeCpm.setUnits(feeUnits);
      patchedFields.add("feeCpm.units");
    }
    if (feeNanos != null) {
      feeCpm.setNanos(feeNanos);
      patchedFields.add("feeCpm.nanos");
    }
    patchedPackage.setFeeCpm(feeCpm);

    return String.join(",", patchedFields);
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
    String curatedPackageId = parsedArgs.getString("curated_package_id");
    String name = String.format("curators/%s/curatedPackages/%s", accountId, curatedPackageId);

    CuratedPackage patchedCuratedPackage = new CuratedPackage();
    String updateMask = buildPatchedCuratedPackage(parsedArgs, patchedCuratedPackage);

    if (updateMask.isEmpty()) {
      System.out.println("No fields were specified to patch. Exiting.");
    } else {
      try {
        execute(client, name, patchedCuratedPackage, updateMask);
      } catch (IOException ex) {
        System.out.printf("Marketplace API returned error response:%n%s", ex);
        System.exit(1);
      }
    }
  }
}
