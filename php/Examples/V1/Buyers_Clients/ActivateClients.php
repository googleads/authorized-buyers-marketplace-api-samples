<?php

/**
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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Clients;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\ActivateClientRequest;

/**
 * This example illustrates how to activate a client with the specified account and client IDs.
 */
class ActivateClients extends BaseExample
{
    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceAuthorizedBuyersMarketplace($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the buyers resource under which the client was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the clients.activate request.',
                'required' => true
            ],
            [
                'name' => 'client_id',
                'display' => 'Client ID',
                'description' =>
                    'The resource ID of the buyers.clients resource for which the client ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the clients.activate request.',
                'required' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/clients/$values[client_id]";

        $activateRequest = new ActivateClientRequest();

        print "<h2>Activating Client '$name':</h2>";
        $result = $this->service->buyers_clients->activate($name, $activateRequest);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Activate Client';
    }
}
