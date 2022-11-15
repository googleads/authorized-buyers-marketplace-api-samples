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
# Lists proposals for the specified buyer.

require 'optparse'

require_relative '../../../util'


def list_proposals(marketplace, options)
  name = "buyers/#{options[:account_id]}"
  filter = options[:filter]
  page_size = options[:page_size]

  page_token = nil

  puts "Listing proposals for buyer with name: \"#{name}\"."
  begin
    response = marketplace.list_buyer_proposals(
        name, filter: filter, page_size: page_size, page_token: page_token
    )

    page_token = response.next_page_token

    unless response.proposals.nil?
      response.proposals.each do |proposal|
        ProposalPrinter.print(proposal)
      end
    else
      puts 'No proposals found.'
    end
  end until page_token.nil?
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
      'The resource ID of the buyers resource under which the proposals were created. This will be used to construct '\
      'the name used as a path parameter for the proposals.list request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'filter',
      'Query string to filter proposals. By default, this example will filter by deal type to retrieve proposals '\
      'including programmatic guaranteed deals to demonstrage usage.',
      type: Array, short_alias: 'f', required: false, default_value: 'dealType = PROGRAMMATIC_GUARANTEED'
    ),
    Option.new(
      'page_size', 'The number of rows to return per page. The server may return fewer rows than specified.',
      type: Array, short_alias: 'p', required: false, default_value: MAX_PAGE_SIZE
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    list_proposals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    puts "Error: #{e}"
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
