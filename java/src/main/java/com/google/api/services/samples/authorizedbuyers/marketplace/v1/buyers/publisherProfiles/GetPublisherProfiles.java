/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.publisherProfiles;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.PublisherProfile;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to get a publisher profile for the given buyer and publisher profile
 * ID.
 */
public class GetPublisherProfiles {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String publisherProfileId = parsedArgs.getString("publisher_profile_id");
    String name = String.format("buyers/%d/publisherProfiles/%s", accountId, publisherProfileId);

    PublisherProfile publisherProfile = null;

    try {
      publisherProfile = marketplaceClient.buyers().publisherProfiles().get(name).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response::%n%s", ex);
      System.exit(1);
    }

    System.out.printf(
        "Found publisher profile with ID \"%s\" for buyer account ID '%d'::%n",
        publisherProfileId, accountId);
    Utils.printPublisherProfile(publisherProfile);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("GetPublisherProfiles")
            .build()
            .defaultHelp(true)
            .description(("Get a publisher profile for the given buyer and publisher profile ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the publisher profile resource "
                + "is being accessed. This will be used to construct the parent used as a path "
                + "parameter for the publisherProfiles.get request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--publisher_profile_id")
        .help(
            "The resource ID of the buyers.publisherProfiles resource that is being accessed. "
                + "This will be used to construct the name used as a path parameter for the "
                + "publisherProfiles.get request.")
        .required(true);

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
      System.out.printf("Unable to create Marketplace API service::%n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport::%n%s", ex);
      System.exit(1);
    }

    execute(client, parsedArgs);
  }
}
