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

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1.Buyers.AuctionPackages
{
    /// <summary>
    /// Gets a single auction package for the given buyer and auction package IDs.
    /// </summary>
    public class GetAuctionPackages : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetAuctionPackages()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example gets a specific auction package for a buyer account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "auction_package_id"};
            bool showHelp = false;

            string accountId = null;
            string auctionPackageId = null;

            OptionSet options = new OptionSet {
                "Get an auction package for the given buyer and auction package IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the auction " +
                     "package is being retrieved. This will be used to construct the name used " +
                     "as a path parameter for the auctionPackages.get request."),
                    a => accountId = a
                },
                {
                    "auction_package_id=",
                    ("[Required] The resource ID of the buyers.auctionPackages resource that " +
                     "is being retrieved. This will be used to construct the name used as a " +
                     "path parameter for the auctionPackages.get request."),
                    auction_package_id => auctionPackageId = auction_package_id
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
            parsedArgs["auction_package_id"] = auctionPackageId;
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
            string auctionPackageId = (string) parsedArgs["auction_package_id"];
            string name = $"buyers/{accountId}/auctionPackages/{auctionPackageId}";

            BuyersResource.AuctionPackagesResource.GetRequest request =
                mkService.Buyers.AuctionPackages.Get(name);
            AuctionPackage response = null;

            Console.WriteLine("Getting auction package with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (Exception exception)
            {
                throw new ApplicationException(
                    $"Marketplace API returned error response:\n{exception.Message}");
            }

            Utilities.PrintAuctionPackage(response);
        }
    }
}
