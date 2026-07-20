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
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class CreateDataSegment {

  private CreateDataSegment() {}

  /**
   * Executes the create operation for a data segment.
   *
   * @param marketplaceClient the initialized Marketplace API client.
   * @param accountId the account ID of the curator that is creating the data segment
   * @param dataSegmentId the id of the data segment to be created
   * @param cpmFeeCurrencyCode the currency code of the data segment's CPM fee
   * @param cpmFeeUnits the whole units of the data segment's CPM fee
   * @param cpmFeeNanos the nanos of the data segment's CPM fee, representing a fraction of the
   *     specified currency.
   */
  public static void execute(
      AuthorizedBuyersMarketplace marketplaceClient,
      Long accountId,
      String dataSegmentId,
      String cpmFeeCurrencyCode,
      Long cpmFeeUnits,
      Integer cpmFeeNanos) throws IOException {

    // Create the required objects to call the create method.
    DataSegment dataSegmentToCreate =
        new DataSegment()
            .setName(String.format("curators/%d/dataSegments/%s", accountId, dataSegmentId))
            .setCpmFee(
                new Money()
                    .setCurrencyCode(cpmFeeCurrencyCode)
                    .setUnits(cpmFeeUnits)
                    .setNanos(cpmFeeNanos));

    String parent = String.format("curators/%d", accountId);

    // Call the API to create the data segment.
    DataSegment createdDataSegment =
        marketplaceClient.curators().dataSegments().create(parent, dataSegmentToCreate).execute();

    System.out.println("Successfully created data segment:");
    Utils.jsonPrettyPrint(createdDataSegment);
  }

  /**
   * Creates and configures the ArgumentParser for this sample.
   *
   * @return the configured ArgumentParser.
   */
  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreateDataSegment")
            .build()
            .defaultHelp(true)
            .description("Creates a new data segment for the given curator account.");

    // Required arguments.
    parser
        .addArgument("-a", "--account_id")
        .help("The account ID of the curator that will create the curated package.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-d", "--data_segment_id")
        .help(
            "The identifier to be used in the name of the data segment to be created."
                + " The final name will be curators/{account_id}/dataSegments/{data_segment_id}")
        .required(true);
    parser
        .addArgument("--cpm_fee_currency_code")
        .help("The three-letter currency code defined in ISO 4217. For example, \"USD\".")
        .required(true);
    parser
        .addArgument("--cpm_fee_units")
        .help(
            "The whole units of the CPM fee. For example, if `currencyCode` were set to \"USD\", "
                + "a value of \"1\" would be $1 USD for 1,000 views. ")
        .type(Long.class)
        .required(true);
    parser
        .addArgument("--cpm_fee_nanos")
        .help(
            "The nano units of the CPM fee, representing a fraction of the specified "
                + "currency. For example, if `currencyCode` were set to \"USD\", a value of "
                + "\"20000000\" would be $0.02 USD for 1,000 views. ")
        .type(Integer.class)
        .required(true);

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

    // Parse arguments.
    Long accountId = parsedArgs.getLong("account_id");
    String dataSegmentId = parsedArgs.getString("data_segment_id");
    String cpmFeeCurrencyCode = parsedArgs.getString("cpm_fee_currency_code");
    Long cpmFeeUnits = parsedArgs.getLong("cpm_fee_units");
    Integer cpmFeeNanos = parsedArgs.getInt("cpm_fee_nanos");

    try {
      execute(client, accountId, dataSegmentId, cpmFeeCurrencyCode, cpmFeeUnits, cpmFeeNanos);
    } catch (Exception e) {
      System.out.printf("Marketplace API returned error response:%n%s", e);
      System.exit(1);
    }
  }
}
