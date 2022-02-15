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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.Proposals.Deals
{
    /// <summary>
    /// Gets a single deal for the given buyer, proposal, and deal IDs.
    /// </summary>
    public class GetDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example gets a deal for given buyer, proposal, and deal IDs.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "deal_id", "proposal_id"};
            bool showHelp = false;

            string accountId = null;
            string dealId = null;
            string proposalId = null;

            OptionSet options = new OptionSet {
                "Get a deal for the given buyer, proposal, and deal IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the deal is " +
                     "being retrieved. This will be used to construct the name used as a path " +
                     "parameter for the deals.get request."),
                    a => accountId = a
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of the buyers.proposals.deals resource that is " +
                     "being retrieved. This will be used to construct the name used as a path " +
                     "parameter for the deals.get request."),
                    d => dealId = d
                },
                {
                    "p|proposal_id=",
                    ("[Required] The resource ID of the buyers.proposals resource under which " +
                     "the deal is being retrieved. This will be used to construct the name used " +
                     "as a path parameter for the deals.get request."),
                    p => proposalId = p
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
            parsedArgs["deal_id"] = dealId;
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
            string dealId = (string) parsedArgs["deal_id"];
            string proposalId = (string) parsedArgs["proposal_id"];
            string name = $"buyers/{accountId}/proposals/{proposalId}/deals/{dealId}";

            BuyersResource.ProposalsResource.DealsResource.GetRequest request =
                mkService.Buyers.Proposals.Deals.Get(name);
            Deal response = null;

            Console.WriteLine("Getting deal with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintDeal(response);
        }
    }
}
