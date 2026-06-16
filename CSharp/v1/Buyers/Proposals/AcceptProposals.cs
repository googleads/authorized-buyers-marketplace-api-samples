/* Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Google.Apis.AuthorizedBuyersMarketplace.v1;
using Google.Apis.AuthorizedBuyersMarketplace.v1.Data;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Proposals
{
    /// <summary>
    /// Accepts a proposal for the given account and proposal IDs.
    ///
    /// Note that a proposal can only be accepted if it is in the BUYER_ACCEPTANCE_REQUESTED
    /// state. Once both a buyer and seller have accepted a proposal, its state will change to
    /// FINALIZED.
    /// </summary>
    public class AcceptProposals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public AcceptProposals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example accepts a given proposal on behalf of the specified buyer.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "proposal_id",
                "proposal_revision"};
            bool showHelp = false;

            string accountId = null;
            string proposalId = null;
            long? proposalRevision = null;

            OptionSet options = new OptionSet {
                "Accepts a proposal for the given account and proposal IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "proposal exists. This will be used to construct the name used as a path " +
                     "parameter for the proposals.accept request."),
                    a => accountId = a
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource that is " +
                     "being accepted. This will be used to construct the name used as a path " +
                     "parameter for the proposals.accept request."),
                    p => proposalId = p
                },
                {
                    "r|proposal_revision=",
                    ("The last known revision number of the proposal. If this is less than the ." +
                     "revision number stored server-side, it means that the proposal revision " +
                     "being worked upon is obsolete, and an error message will be returned."),
                    (long? r) => proposalRevision = r
                },
            };

            List<string> extras = options.Parse(exampleArgs);
            var parsedArgs = new Dictionary<string, object>();

            // Show help message.
            if (showHelp == true)
            {
                options.WriteOptionDescriptions(Console.Out);
                Environment.Exit(0);
            }
            // Set optional arguments.
            parsedArgs["account_id"] = accountId;
            parsedArgs["proposal_id"] = proposalId;
            parsedArgs["proposal_revision"] = proposalRevision;
            // Validate that options were set correctly.
            Utilities.ValidateOptions(options, parsedArgs, requiredOptions, extras);

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string accountId = (string) parsedArgs["account_id"];
            string proposalId = (string) parsedArgs["proposal_id"];
            string name = $"buyers/{accountId}/proposals/{proposalId}";

            AcceptProposalRequest acceptProposalRequest = new AcceptProposalRequest()
            {
                ProposalRevision = (long?) parsedArgs["proposal_revision"]
            };

            BuyersResource.ProposalsResource.AcceptRequest request =
                mkService.Buyers.Proposals.Accept(acceptProposalRequest, name);
            Proposal response = null;

            Console.WriteLine("Accepting a proposal with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintProposal(response);
        }
    }
}
