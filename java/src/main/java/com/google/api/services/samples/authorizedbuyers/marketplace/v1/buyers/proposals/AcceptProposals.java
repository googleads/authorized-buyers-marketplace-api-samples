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

package com.google.api.services.samples.authorizedbuyers.marketplace.v1.buyers.proposals;

import com.google.api.services.authorizedbuyersmarketplace.v1.AuthorizedBuyersMarketplace;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.AcceptProposalRequest;
import com.google.api.services.authorizedbuyersmarketplace.v1.model.Proposal;
import com.google.api.services.samples.authorizedbuyers.marketplace.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Accepts a proposal for the given account and proposal IDs.
 *
 * <p>Note that a proposal can only be accepted if it is in the BUYER_ACCEPTANCE_REQUESTED state.
 * Once both a buyer and seller have accepted a proposal, its state will change to FINALIZED.
 */
public class AcceptProposals {

  public static void execute(AuthorizedBuyersMarketplace marketplaceClient, Namespace parsedArgs) {
    Long accountId = parsedArgs.getLong("account_id");
    String proposalId = parsedArgs.getString("proposal_id");
    String name = String.format("buyers/%d/proposals/%s", accountId, proposalId);

    AcceptProposalRequest acceptProposalRequest = new AcceptProposalRequest();
    acceptProposalRequest.setProposalRevision(parsedArgs.getLong("proposal_revision"));

    Proposal proposal = null;

    try {
      proposal =
          marketplaceClient.buyers().proposals().accept(name, acceptProposalRequest).execute();
    } catch (IOException ex) {
      System.out.printf("Marketplace API returned error response:%n%s", ex);
      System.exit(1);
    }

    System.out.printf("Accepting proposal with name \"%s\":%n", name);
    Utils.printProposal(proposal);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("AcceptProposals")
            .build()
            .defaultHelp(true)
            .description(("Accepts a proposal for the given account and proposal IDs."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the proposal exists. This will "
                + "be used to construct the name used as a path parameter for the proposals.accept "
                + "request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--proposal_id")
        .help(
            "The resource ID of the buyers.proposals resource that is being accepted. This "
                + "will be used to construct the name used as a path parameter for the "
                + "proposals.accept request.")
        .required(true);
    parser
        .addArgument("-r", "--proposal_revision")
        .help(
            "The last known revision number of the proposal. If this is less than the "
                + "revision number stored server-side, it means that the proposal revision being "
                + "worked upon is obsolete, and an error message will be returned.")
        .required(true)
        .type(Long.class);

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

    execute(client, parsedArgs);
  }
}
