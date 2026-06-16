#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Gets a single publisher profile for the given account and profile IDs.

require 'optparse'

require_relative '../../../util'


def get_publisher_profiles(marketplace, options)
  name = "buyers/#{options[:account_id]}/publisherProfiles/#{options[:publisher_profile_id]}"

  puts "Get publisher profile with name '#{name}'"

  publisher_profile = marketplace.get_buyer_publisher_profile(name)
  PublisherProfilePrinter.print(publisher_profile)
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the publisherProfiles resource is being accessed. This '\
      'will be used to construct the name used as a path parameter for the publisherProfiles.get request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'publisher_profile_id',
      'The resource ID of the buyers.publisherProfiles resource that is being accessed. This will be used to '\
      'construct the name used as a path parameter for the publisherProfiles.get request.',
      short_alias: 'c', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    get_publisher_profiles(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
