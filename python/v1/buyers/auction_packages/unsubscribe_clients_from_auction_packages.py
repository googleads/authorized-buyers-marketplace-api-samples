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

"""Unsubscribes a given set of clients from a specified auction package."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_AUCTION_PACKAGE_NAME_TEMPLATE = 'buyers/%s/auctionPackages/%s'
_CLIENT_NAME_TEMPLATE = 'buyers/%s/clients/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_AUCTION_PACKAGE_RESOURCE_ID = 'ENTER_CLIENT_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id
    auction_package_name = _AUCTION_PACKAGE_NAME_TEMPLATE % (
        account_id, args.auction_package_id)

    body = {'clients': [_CLIENT_NAME_TEMPLATE % (account_id, client)
                        for client in args.client_ids]}

    print('Unsubscribing clients from auction package '
          f'"{auction_package_name}" on behalf of buyer account w/ ID '
          f'"{account_id}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().auctionPackages().unsubscribeClients(
            auctionPackage=auction_package_name, body=body).execute()
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

    parser = argparse.ArgumentParser(
        description=('Unsubscribe a given buyer\'s clients from a specified '
                     'auction package.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource that is to have its '
              'clients unsubscribed from an auction package. This will be used '
              'to construct the name used as a path parameter for the '
              'auctionPackages.unsubscribeClients request.'))
    parser.add_argument(
        '-p', '--auction_package_id',
        default=DEFAULT_AUCTION_PACKAGE_RESOURCE_ID,
        help=('The resource ID of the buyers.auctionPackages resource that the '
              'buyer will unsubscribe one or more of its clients from. This '
              'will be used to construct the name used as a path parameter for '
              'the auctionPackages.unsubscribeClients request.'))
    parser.add_argument(
        '-c', '--client_ids', nargs='*',
        help=('The resource IDs of the buyers.clients resources that are to '
              'be unsubscribed from the auction package. Specify each client '
              'ID separated by a space.'))

    main(service, parser.parse_args())
