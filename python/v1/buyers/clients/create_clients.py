#!/usr/bin/python
#
# Copyright 2021 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Creates a client for the given buyer account ID."""


import argparse
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


_BUYER_NAME_TEMPLATE = 'buyers/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id

    client = {
        'displayName': args.display_name,
        'role': args.role,
        'sellerVisible': args.seller_visible
    }

    partner_client_id = args.partner_client_id
    if partner_client_id:
        client['partnerClientId'] = partner_client_id

    print(f'Creating client for buyer account ID "{account_id}":')
    try:
        # Construct and execute the request.
        response = (marketplace.buyers().clients().create(
            parent=_BUYER_NAME_TEMPLATE % account_id, body=client).execute())
    except HttpError as e:
        print(e)
        sys.exit(1)

    pprint.pprint(response)


if __name__ == '__main__':
    try:
        service = util.get_service(version='v1')
    except IOError as ex:
        print(f'Unable to create marketplace service - {ex}')
        print('Did you specify the key file in util.py?')
        sys.exit(1)

    def valid_role(value):
        valid_roles = (
            'CLIENT_DEAL_VIEWER', 'CLIENT_DEAL_VIEWER',
            'CLIENT_DEAL_NEGOTIATOR', 'CLIENT_DEAL_APPROVER')

        upper_input = value.upper()

        if upper_input in valid_roles:
            return upper_input
        else:
            raise ValueError(
                f'Invalid role specified. Must be one of: {valid_roles}')

    def valid_bool(value):
        upper_input = value.upper()

        if upper_input == 'TRUE':
            return True
        elif upper_input == 'FALSE':
            return False
        else:
            raise ValueError(
                'Invalid value specified. Must be a boolean input.')

    parser = argparse.ArgumentParser(
        description='Creates a client for the given buyer account ID.')

    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the '
              'client is to be created.'))
    # Optional fields.
    parser.add_argument(
        '-n', '--display_name', default='Test Client #%s' % uuid.uuid4(),
        help=('The display name shown to publishers. Must be unique for '
              'clients without partnerClientId specified. The maximum length '
              'allowed is 255 characters. By default, this sample will '
              'specify a generated name.'))
    parser.add_argument(
        '-p', '--partner_client_id', default=None,
        help=('Arbitrary unique identifier provided by the buyer. This field '
              'can be used to associate a client with an identifier in the '
              'namespace of the buyer. If present, it must be unique across '
              'all the clients. Be default, this sample will not specify a '
              'partnerClientId.'))
    parser.add_argument(
        '-r', '--role', default='CLIENT_DEAL_VIEWER', type=valid_role,
        help=('The role assigned to the client, which determines its '
              'permissions. By default, this will be set to '
              'CLIENT_DEAL_VIEWER. For more details on how to interpret the '
              'different roles, see: https://developers.google.com/'
              'authorized-buyers/apis/marketplace/reference/rest/v1/'
              'buyers.clients#ClientRole'))
    parser.add_argument(
        '-s', '--seller_visible', default=False, type=valid_bool,
        help=('Whether the client will be visible to publishers. By default, '
              'this sample will set this to False.'))

    main(service, parser.parse_args())
