/* Copyright 2021 Google LLC
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
using Google.Apis.Auth.OAuth2;
using Google.Apis.Json;
using Google.Apis.Services;

using System;
using System.Collections.Generic;

namespace Google.Apis.AuthorizedBuyersMarketplace.Examples.v1
{
    /// <summary>
    /// Self contained sample to return a list of clients for a given buyer account.
    /// Primarily used by the Getting Started guide:
    /// https://developers.google.com/authorized-buyers/apis/getting_started
    ///
    /// Note: To run this sample, you will need to configure it as the StartupObject in
    /// Google.Apis.AuthorizedBuyersMarketplace.Examples.csproj.
    /// </summary>
    internal class FirstApiRequest
    {
        private static void Main(string[] args)
        {
            // See the README.md for details of these fields.
            // Retrieved from https://console.developers.google.com
            var ServiceKeyFilePath = "PATH TO JSON KEY FILE HERE";

            // Name of the buyer resource for which the API call is being made.
            var buyerName = "INSERT_BUYER_RESOURCE_NAME_HERE";

            // Retrieve credential parameters from the key JSON file.
            var credentialParameters = NewtonsoftJsonSerializer.Instance
                .Deserialize<JsonCredentialParameters>(
                    System.IO.File.ReadAllText(ServiceKeyFilePath));

            // Create the credentials.
            var credentialInitializer = new ServiceAccountCredential.Initializer(
                    credentialParameters.ClientEmail)
                {
                    Scopes = new[]
                    {
                        AuthorizedBuyersMarketplaceService.Scope.AuthorizedBuyersMarketplace
                    }
                }.FromPrivateKey(credentialParameters.PrivateKey);

            var oAuth2Credentials = new ServiceAccountCredential(credentialInitializer);

            // Use the credentials to create a client for the API service.
            var serviceInitializer = new BaseClientService.Initializer
                {
                    HttpClientInitializer = oAuth2Credentials,
                    ApplicationName = "FirstAPICall"
                };

            var mkService = new AuthorizedBuyersMarketplaceService(serviceInitializer);

            // Call the buyers.clients.list method to list clients for the given buyer.
            BuyersResource.ClientsResource.ListRequest request =
                mkService.Buyers.Clients.List(buyerName);

            IList<Client> clients = request.Execute().Clients;

            foreach (Client client in clients)
            {
                Console.WriteLine("* Client name: {0}", client.Name);
            }

            Console.ReadLine();
        }
    }
}
