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

"""Activates client with the given with the given account ID and client ID."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_CLIENTS_NAME_TEMPLATE = 'buyers/%s/clients/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_CLIENT_RESOURCE_ID = 'ENTER_CLIENT_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id
    client_id = args.client_id

    print(f'Activate client "{client_id}" for account "{account_id}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().clients().activate(
            name=_CLIENTS_NAME_TEMPLATE % (account_id, client_id)).execute()
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
        description=('Activate a client for the given buyer account ID and '
                     'client ID.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the '
              'client was created. This will be used to construct the '
              'name used as a path parameter for the clients.activate request.'
              ))
    parser.add_argument(
        '-c', '--client_id', default=DEFAULT_CLIENT_RESOURCE_ID,
        help=('The resource ID of the buyers.clients resource for which the '
              'client was created. This will be used to construct the '
              'name used as a path parameter for the clients.activate request.'
              ))

    main(service, parser.parse_args())
