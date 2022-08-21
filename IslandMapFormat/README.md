# .ismap file format

## Usage
 
The main goal of this file structure is to store a world into a single file.

## Global file structure
`ux` represent unsigned `x` byte.  
`sx` represent signed `x` byte.

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th colspan="3">Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>writer_version</td>
    <td colspan="3">u1 (currently 0)</td>
  </tr>
  <tr>
    <td>map_type</td>
    <td colspan="3">u1</td>
  </tr>
  <tr>
    <td rowspan="3">minecraft_version</td>
    <td rowspan="3">u3</td>
    <td style="font-style:italic">major</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">minor</td>
    <td>u1</td>
  </tr>
  <tr>
    <td style="font-style:italic">revision</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>creation_date</td>
    <td colspan="3">u8</td>
  </tr>
  <tr>
    <td>island_statistics</td>
    <td colspan="3"><a href="#Stats-structure">stats</a></td>
  </tr>
  <tr>
    <td>island</td>
    <td colspan="3"><a href="#World-structure">world</a>/<a href="#Sector-structure">sector</a> (map_type=0/1)</td>
  </tr>
  <tr>
    <td>dim_count</td>
    <td colspan="3">u1</td>
  </tr>
  <tr>
    <td>dims</td>
    <td colspan="3"><a href="#Dim-structure">dim</a>[dim_count]</td>
  </tr>
</tbody>
</table>


## Stats structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>spawn_location</td>
    <td><a href="#Location-structure">location</a></td>
  </tr>
  <tr>
    <td>member_spawn_location</td>
    <td><a href="#Location-structure">location</a></td>
  </tr>
  <tr>
    <td>players_count</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>players</td>
    <td><a href="#Player-structure">player</a>[players_count]</td>
  </tr>
</tbody>
</table>

# Dim structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
  <tr>
    <td>region_count</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>regions</td>
    <td><a href="#Region-structure">region</a>[region_count]</td>
  </tr>
</table>

## Player structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>uuid</td>
    <td>u16</td>
  </tr>
  <tr>
    <td>member_since</td>
    <td>u8</td>
  </tr>
  <tr>
    <td>play_time</td>
    <td>u8</td>
  </tr>
  <tr>
    <td>permission_level</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>is_flying</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>dim</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>food_level</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>food_exhaustion_level</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>food_saturation_level</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>food_tick_timer</td>
    <td>u1</td>
  </tr>
  <tr>
    <td>ridden_entity</td>
    <td><a href="#NBT-structure">nbt</a></td>
  </tr>
  <tr>
    <td>shoulder_entity_left</td>
    <td><a href="#NBT-structure">nbt</a></td>
  </tr>
  <tr>
    <td>shoulder_entity_right</td>
    <td><a href="#NBT-structure">nbt</a></td>
  </tr>
  <tr>
    <td>xp_level</td>
    <td>u4</td>
  </tr>
  <tr>
    <td>xp_percent</td>
    <td>u4</td>
  </tr>
  <tr>
    <td>xp_seed</td>
    <td>u4</td>
  </tr>
  <tr>
    <td>xp_total</td>
    <td>u4</td>
  </tr>
</tbody>
</table>

## Region structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>x</td>
    <td>s4</td>
  </tr>
  <tr>
    <td>z</td>
    <td>s4</td>
  </tr>
  <tr>
    <td>region</td>
    <td><a href="https://minecraft.fandom.com/wiki/Anvil_file_format">.mca</a> file</td>
  </tr>
</tbody>
</table>

## Sector structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>location</td>
    <td><a href="#Location-structure">location</a></td>
  </tr>
  <tr>
    <td>width</td>
    <td>u2</td>
  </tr>
  <tr>
    <td>length</td>
    <td>u2</td>
  </tr>
</tbody>
</table>

## Location structure

<table style="text-align:center">
<thead>
  <tr>
    <th>Name</th>
    <th>Byte</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>x</td>
    <td>s8</td>
  </tr>
  <tr>
    <td>y</td>
    <td>s4</td>
  </tr>
  <tr>
    <td>z</td>
    <td>s8</td>
  </tr>
</tbody>
</table>

## NBT structure

<table style="text-align:center">
<thead>
  <th>Name</th>
  <th>Byte</th>
</thead>
<tbody>
  <tr>
    <td>length</td>
    <td>u2</td>
  </tr>
  <tr>
    <td>bytes</td>
    <td>byte[length]</td>
  </tr>
</tbody>
</table>