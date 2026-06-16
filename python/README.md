# Authorized Buyers Marketplace API Python Samples

These samples demonstrate basic usage of the Authorized Buyers Marketplace API.

# Getting Started



To run these samples, you'll need to do the following:

1. If you have not done so already, you must have Python 3.6+ installed. It is
   recommended that you use virtual environment utilities like
   [pyenv](https://github.com/pyenv/pyenv#simple-python-version-management-pyenv)
   if you need to manage multiple Python versions.
1. Download and install the dependencies used in these samples by running the
   following command:

   ```
   $ python -m pip install -r requirements.txt
   ```
   **Note:** If you do not have pip installed, you can find installation
   instructions [here](https://pip.pypa.io/en/stable/installing/#installation).
1. If you haven't done so already, you should read about the OAuth2
   [Service Account Flow](https://developers.google.com/accounts/docs/OAuth2ServiceAccount)
   because that is the flow used by these samples for authorization to access
   the Authorized Buyers Marketplace API.
1. Go to the [Google Developers Console](https://console.developers.google.com/)
   and open the menu. Click the `APIs & Services` option and then the
   `Credentials` tab. From there, you can either click the `Create credentials`
   button to create a new Service Account for use with this project, or find an
   existing one under `Manage service accounts`.
1. In the `Manage service accounts` page, you can generate a new key for the
   Service Account by clicking the ellipses in the proper row and selecting
   `Create key` in the resulting menu. This library only supports the JSON key
   files, so select that as the `Key type`. Click the `create` button and the
   file will be generated and downloaded to your computer. Place the JSON file
   in the sample directory. You will need the Service Account email on this
   page for the next step.
1. To authorize a Service Account to access your Authorized Buyers account via
   the API, go to the [Authorized Buyers UI](https://www.google.com/authorizedbuyers)
   and click the `Admin` drop-down menu in the navigation bar corner,
   and then click the `Account users` option. Click the `Link Service Account`
   button on this page and enter the Service Account's email to associate it
   with your Authorized Buyers Account.
1. Open **util.py** and update the `KEY_FILE` field to represent the
   path to the JSON key file you downloaded earlier. If you placed it in the
   sample directory, this should just be the filename.
1. Before attempting to run any of the samples, you should update any fields
   containing default values for data sent in API requests. Alternatively, you
   can also provide values for these fields at run-time as command-line
   arguments.

You should now be able to start any of the samples by running them from the
command line.

