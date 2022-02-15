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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.FinalizedDeals
{
    /// <summary>
    /// Signals that the given finalized deal is ready to serve.
    ///
    /// By default, deals are set ready to serve as soon as they're finalized. For programmatic
    /// guaranteed deals, bidders can opt out of this feature by asking their account manager. This
    /// is recommended for programmatic guaranteed deals in order to ensure that bidders have
    /// creatives prepared to be used in placing bids once the deal is serving. Use
    /// finalizedDeals.addCreative to associate creatives with a programmatic guaranteed deal.
    /// </summary>
    public class SetReadyToServeFinalizedDeals : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public SetReadyToServeFinalizedDeals()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example signals that the given finalized deal is ready to serve.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "deal_id"};
            bool showHelp = false;

            string accountId = null;
            string dealId = null;

            OptionSet options = new OptionSet {
                "Signals that the given finalized deal is ready to serve.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "finalized deal was created. This will be used to construct the name used " +
                     "as a path parameter for the finalizedDeals.setReadyToServe request."),
                    a => accountId = a
                },
                {
                    "d|deal_id=",
                    ("[Required] The resource ID of the buyers.finalizedDeals resource that is " +
                     "you intend to signal is ready to serve. This will be used to construct " +
                     "the name used as a path parameter for the finalizedDeals.setReadyToServe " +
                     "request."),
                    d => dealId = d
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
            string name = $"buyers/{accountId}/finalizedDeals/{dealId}";

            BuyersResource.FinalizedDealsResource.SetReadyToServeRequest request =
                mkService.Buyers.FinalizedDeals.SetReadyToServe(new SetReadyToServeRequest(), name);
            FinalizedDeal response = null;

            Console.WriteLine("Signaling that finalized deal with name \"{0}\" is ready to serve.",
                name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintFinalizedDeal(response);
        }
    }
}
