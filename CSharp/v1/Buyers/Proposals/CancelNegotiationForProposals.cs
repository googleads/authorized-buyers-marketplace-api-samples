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
    /// Cancels the ongoing negotiation for the specified proposal.
    ///
    /// This method is not supported for proposals including private auction deals because
    /// negotiation for that deal type can not be canceled. On successful cancelation, the
    /// proposal's state will be set to TERMINATED.
    ///
    /// This does not cancel or end serving for deals that have already been finalized. For
    /// finalized deals that are under renegotiation, calling this method will instead reset the
    /// proposal's state to FINALIZED.
    /// </summary>
    public class CancelNegotiationForProposals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CancelNegotiationForProposals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example cancels negotiaton for a given proposal.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "proposal_id"};
            bool showHelp = false;

            string accountId = null;
            string proposalId = null;

            OptionSet options = new OptionSet {
                "Cancels negotiation for a proposal with the given account and proposal IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "proposal exists. This will be used to construct the name used as a path " +
                     "parameter for the proposals.cancelNegotiation request."),
                    a => accountId = a
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource that is " +
                     "being canceled. This will be used to construct the name used as a path " +
                     "parameter for the proposals.cancelNegotiation request."),
                    p => proposalId = p
                }
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

            BuyersResource.ProposalsResource.CancelNegotiationRequest request =
                mkService.Buyers.Proposals.CancelNegotiation(new CancelNegotiationRequest(), name);
            Proposal response = null;

            Console.WriteLine("Canceling negotiation for a proposal with name: {0}", name);

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
