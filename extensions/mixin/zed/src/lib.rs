use std::{fs::File, io::Write};

use zed_extension_api::{
    self, LanguageServerId, LanguageServerInstallationStatus, serde_json::json,
    set_language_server_installation_status, settings::LspSettings,
};

const ECLIPSE_JDT_LANGUAGE_SERVER_NAME: &str = "Eclipse JDT Language Server";

struct Mixin {
    cached_binary_path: Option<String>,
}

impl Mixin {
    const fn new() -> Self {
        Mixin {
            cached_binary_path: None,
        }
    }

    fn language_server_bundle_path(
        &mut self,
        language_server_id: &LanguageServerId,
        worktree: &zed_extension_api::Worktree,
    ) -> String {
        if let Some(cached_binary_path) = self.cached_binary_path.clone() {
            return cached_binary_path;
        }

        // TODO: Switch to include_glob!(...), see https://crates.io/crates/include_glob
        //  This prevents having to update the version number of the JAR file
        // TODO: Update the language server when a newer version is included
        let language_server_file = File::create_new("language_server.jar");
        if let Ok(mut language_server_file) = language_server_file {
            set_language_server_installation_status(
                language_server_id,
                &LanguageServerInstallationStatus::Downloading,
            );

            let language_server =
                include_bytes!("../../../../language_server/build/libs/language_server-0.1.0.jar");
            language_server_file.write_all(language_server);
            self.cached_binary_path = Some(String::from("language_server.jar"));

            let eclipse_jdt_language_server_lsp_settings =
                LspSettings::for_worktree(ECLIPSE_JDT_LANGUAGE_SERVER_NAME, worktree);
            if let Ok(eclipse_jdt_language_server_lsp_settings) =
                eclipse_jdt_language_server_lsp_settings
            {
                let mut initialization_options =
                    eclipse_jdt_language_server_lsp_settings.initialization_options;
                if initialization_options.is_none() {
                    initialization_options = Some(json!("[]"))
                }

                let mut initialization_options =
                    initialization_options.expect("the initialization options should have a value");
                let bundles = initialization_options.get_mut("bundles");
                match bundles {
                    Some(bundles) => bundles
                        .as_array_mut()
                        .expect("bundles should be an array")
                        .push(json!("language_server.jar")),
                    None => todo!(),
                }
            }

            set_language_server_installation_status(
                language_server_id,
                &LanguageServerInstallationStatus::None,
            );
        }

        self.cached_binary_path
            .clone()
            .expect("the binary path should be cached")
    }
}

impl zed_extension_api::Extension for Mixin {
    fn new() -> Self
    where
        Self: Sized,
    {
        return Mixin::new();
    }

    fn language_server_command(
        &mut self,
        language_server_id: &zed_extension_api::LanguageServerId,
        worktree: &zed_extension_api::Worktree,
    ) -> zed_extension_api::Result<zed_extension_api::Command> {
        Ok(zed_extension_api::Command {
            command: format!(
                "./{}",
                self.language_server_bundle_path(language_server_id, worktree)
            ),
            args: Vec::new(),
            env: Vec::new(),
        })
    }
}

zed_extension_api::register_extension!(Mixin);
