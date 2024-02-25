package dev.huli.hulisfabricservervote.permissions

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.permission.CobblemonPermission
import com.cobblemon.mod.common.api.permission.PermissionLevel
import net.minecraft.command.CommandSource

object ServerVotePermissions {
    lateinit var RELOAD_PERMISSION : CobblemonPermission

    fun invoke(){
        this.RELOAD_PERMISSION = CobblemonPermission("hulisfabricservervote.command.reload",
            PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS)
    }

    fun checkPermission(src:CommandSource, permission: CobblemonPermission): Boolean {
        return Cobblemon.permissionValidator.hasPermission(src,permission)
    }
}