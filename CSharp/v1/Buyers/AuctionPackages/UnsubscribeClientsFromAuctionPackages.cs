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
    /// Unsubscribes one or more clients from a specified auction package.
    /// </summary>
    public class UnsubscribeClientsFromAuctionPackages : ExampleBase
    {
        private AuthorizedBuyersMarketplaceService mkService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public UnsubscribeClientsFromAuctionPackages()
        {
            mkService = Utilities.GetAuthorizedBuyersMarketplaceService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example unsubscribes one or more clients from an auction package.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "auction_package_id", "client_ids"};
            bool showHelp = false;

            string accountId = null;
            string auctionPackageId = null;
            IList<string> clientIds = new List<string>();

            OptionSet options = new OptionSet {
                "Unsubscribe one or more clients from the specified auction package.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the clients " +
                     "unsubscribing from the auction package exist. This will be used to " +
                     "construct the name used as a path parameter for the " +
                     "auctionPackages.unsubscribeClients request."),
                    a => accountId = a
                },
                {
                    "auction_package_id=",
                    ("[Required] The resource ID of the buyers.auctionPackages resource that " +
                     "the buyer is unsubscribing their clients from. This will be used to " +
                     "construct the name used as a path parameter for the " +
                     "auctionPackages.unsubscribeClients request."),
                    auction_package_id => auctionPackageId = auction_package_id
                },
                {
                    "c|client_id=",
                    ("[Required] The resource IDs of one or more clients existing under the " +
                     "buyer that will be unsubscribed from the auction package. These will be " +
                     "used to construct client names that will be passed in the body of the " +
                     "auctionPackages.unsubscribeClients request. Specify this argument for each " +
                     "client you intend to unsubscribe from the auction package."),
                    c => clientIds.Add(c)
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
            parsedArgs["client_ids"] = clientIds;
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
            List<string> clientIds = (List<string>) parsedArgs["client_ids"];
            List<string> clientNames = new List<string>();
            string name = $"buyers/{accountId}/auctionPackages/{auctionPackageId}";

            Console.WriteLine("Unsubscribing the following clients for buyer \"{0}\" from " +
                "auction package \"{1}\":", accountId, auctionPackageId);

            foreach (string clientId in clientIds)
            {
                string clientName = $"buyers/{accountId}/clients/{clientId}";
                clientNames.Add(clientName);
                Console.WriteLine($"- {clientName}");
            }

            UnsubscribeClientsRequest unsubscribeClientsRequest = new UnsubscribeClientsRequest()
            {
                Clients = clientNames
            };

            BuyersResource.AuctionPackagesResource.UnsubscribeClientsRequest request =
                mkService.Buyers.AuctionPackages.UnsubscribeClients(
                    unsubscribeClientsRequest, name);
            AuctionPackage response = null;

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
