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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.curators.dataSegments;

import com.google.api.services.authorizedbuyersmarketplace.v1beta.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.DataSegment;
import com.google.api.services.authorizedbuyersmarketplace.v1beta.model.Money;
import com.google.api.services.samples.authorizedbuyers.marketplace.v1beta.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class PatchDataSegment {

  private PatchDataSegment() {}

  /**
   * Executes the patch operation for a data segment.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param accountId the account ID of the curator that created the data segment.
   * @param dataSegmentId the resource ID of the data segment to update.
   * @param cpmFeeUnits the modified value of `DataSegment.cpmFee.units` (or null if the field is
   *     not being modified).
   * @param cpmFeeNanos the modified value of `DataSegment.cpmFee.nanos` (or null if the field is
   *     not being modified).
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient,
      Long accountId,
      String dataSegmentId,
      Long cpmFeeUnits,
      Integer cpmFeeNanos)
      throws IOException {

    // Construct resources needed for the patch request.
    String name = String.format("curators/%d/dataSegments/%s", accountId, dataSegmentId);

    List<String> patchedFields = new ArrayList<>();
    DataSegment patchedDataSegment = new DataSegment();
    Money cpmFee = new Money();
    if (cpmFeeUnits != null) {
      cpmFee.setUnits(cpmFeeUnits);
      patchedFields.add("cpmFee.units");
    }
    if (cpmFeeNanos != null) {
      cpmFee.setNanos(cpmFeeNanos);
      patchedFields.add("cpmFee.nanos");
    }
    patchedDataSegment.setCpmFee(cpmFee);
    String updateMask = String.join(",", patchedFields);

    // Patch the data segment.
    DataSegment returnedDataSegment =
        marketplaceClient
            .curators()
            .dataSegments()
            .patch(name, patchedDataSegment)
            .setUpdateMask(updateMask)
            .execute();

    System.out.println("Successfully patched data segment:");
    Utils.jsonPrettyPrint(returnedDataSegment);
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchDataSegment")
            .build()
            .defaultHelp(true)
            .description(
                "Updates a specific data segment. To modify the state of a "
                    + "data segment, use the curators.dataSegments.activate or "
                    + "curators.dataSegments.deactivate methods instead.");

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that created the data segment.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--data_segment_id")
        .help("The resource ID of the data segment to update.")
        .required(true);

    // Optional arguments.
    // (Note: the currency code of the cpm fee is not modifiable, and thus is not included here.)
    parser
        .addArgument("--cpm_fee_units")
        .help(
            "The modified value of `DataSegment.cpmFee.units`. This represents the whole units of "
                + "the CPM fee. For example, if `currencyCode` were set to \"USD\", "
                + "a value of \"1\" would be $1 USD for 1,000 views. ")
        .type(Long.class);
    parser
        .addArgument("--cpm_fee_nanos")
        .help(
            "The modified value of `DataSegment.cpmFee.nanos`. This represents the nano units of "
                + "the CPM fee, representing a fraction of the specified currency. "
                + "For example, if `currencyCode` were set to \"USD\", a value of "
                + "\"20000000\" would be $0.02 USD for 1,000 views. ")
        .type(Integer.class);

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

    // Get parsed arguments.
    Long accountId = parsedArgs.getLong("account_id");
    String dataSegmentId = parsedArgs.getString("data_segment_id");
    Long cpmFeeUnits = parsedArgs.getLong("cpm_fee_units");
    Integer cpmFeeNanos = parsedArgs.getInt("cpm_fee_nanos");

    try {
      execute(client, accountId, dataSegmentId, cpmFeeUnits, cpmFeeNanos);
    } catch (Exception e) {
      System.out.printf("Marketplace API returned error response:%n%s", e);
      System.exit(1);
    }
  }
}
