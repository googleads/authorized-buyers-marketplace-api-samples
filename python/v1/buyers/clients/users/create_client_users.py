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

"""Creates a client user for the given buyer account ID and client ID.

When a client user is created, the specified email address will receive an
email to confirm access to the Authorized Buyers UI. It will remain in the
"INVITED" state and be unable to access the UI until the specified email has
approved of the change.
"""


import argparse
import os
import pprint
import random
import sys

sys.path.insert(0, os.path.abspath('../../../..'))

from googleapiclient.errors import HttpError

import util


_CLIENT_NAME_TEMPLATE = 'buyers/%s/clients/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_CLIENT_RESOURCE_ID = 'ENTER_CLIENT_RESOURCE_ID_HERE'


def main(marketplace, args):
  account_id = args.account_id
  client_name = _CLIENT_NAME_TEMPLATE % (args.account_id, args.client_id)

  client_user = {
      'email': args.email
  }

  print(f'Creating client user for client with name "{client_name}":')
  try:
    # Construct and execute the request.
    response = (marketplace.buyers().clients().users().create(
        parent=client_name, body=client_user).execute())
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
      description=('Create a client user for the given buyer account ID and '
                   'client ID.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'client user is to be created.'))
  parser.add_argument(
      '-c', '--client_id', default=DEFAULT_CLIENT_RESOURCE_ID,
      help=('The resource ID of the clients resource under which the '
            'client user is to be created.'))
  # Optional fields.
  parser.add_argument(
      '-e', '--email',
      default=f'testemail{random.randint(10000000, 99999999)}@test.com',
      help=('The client user\'s email address that has to be unique across all '
            'client users for a given client. By default, this will be set to '
            'a randomly generated email for demonstration purposes.'))

  main(service, parser.parse_args())
