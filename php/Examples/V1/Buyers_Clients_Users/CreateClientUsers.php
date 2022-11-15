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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Clients_Users;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\ClientUser;

/**
 * This example illustrates how to create a client user for the given buyer ID, client ID, and
 * email.
 *
 * When a client user is created, the specified email address will receive an email to
 * confirm access to the Authorized Buyers UI. It will remain in the "INVITED" state and be
 * unable to access the UI until the specified email has approved of the change.
 */
class CreateClientUsers extends BaseExample
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
                    'The resource ID of the buyers resource under which the client user is to ' .
                    'be created.',
                'required' => true
            ],
            [
                'name' => 'client_id',
                'display' => 'Client ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.clients resource under which the client ' .
                    'user is to be created.'
            ],
            [
                'name' => 'email',
                'display' => 'Email',
                'description' =>
                    'The client user\'s email address that has to be unique across all client ' .
                    'users for a given client. By default, this will be set to a randomly ' .
                    'generated email for demonstration purposes.',
                'required' => false,
                'default' => 'testemail' . random_int(10000000, 99999999) . '@test.com'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $parentName = "buyers/$values[account_id]/clients/$values[client_id]";

        $newuser = new ClientUser();
        $newuser->email = $values['email'];

        print "<h2>Creating client user for '$parentName':</h2>";
        $result = $this->service->buyers_clients_users->create($parentName, $newuser);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Create Client Users';
    }
}
